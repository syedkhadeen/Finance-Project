import React, { useState } from 'react';
import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
  Typography,
  Grid,
  Paper,
  Divider,
  Tooltip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs, { Dayjs } from 'dayjs';
import axios from 'axios';
import { toast } from 'react-toastify';
import SearchIcon from '@mui/icons-material/Search';
import CalculateIcon from '@mui/icons-material/Calculate';
import ClearIcon from '@mui/icons-material/Clear';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import SaveIcon from '@mui/icons-material/Save';
import SearchModal from './SearchModal';
import ResultSections from './ResultSections';

interface CurvePoint {
  tenor: string;
  rate: string;
  stdDev: string;
}

interface CustomCurveModalProps {
  open: boolean;
  onClose: () => void;
  onSave: (points: CurvePoint[], curveName: string, curveDate: string | null, spotCurve: boolean) => void;
  initialPoints?: CurvePoint[];
}

const CustomCurveModal = ({ open, onClose, onSave, initialPoints = [] }: CustomCurveModalProps) => {
  const [points, setPoints] = useState<CurvePoint[]>(initialPoints);
  const [curveName, setCurveName] = useState('');
  const [curveDate, setCurveDate] = useState<Dayjs | null>(dayjs());
  const [spotCurve, setSpotCurve] = useState<string>('Yes');

  const [tenor, setTenor] = useState('');
  const [rate, setRate] = useState('');
  const [stdDev, setStdDev] = useState('');

  const handleAddPoint = () => {
    if (!tenor.trim() || !rate.trim()) {
      toast.warning('Tenor and Rate are required');
      return;
    }
    setPoints((prev) => [...prev, { tenor, rate, stdDev: stdDev.trim() || '0' }]);
    setTenor('');
    setRate('');
    setStdDev('');
  };

  const handleRemovePoint = (index: number) => {
    setPoints((prev) => prev.filter((_, i) => i !== index));
  };

  const handleSave = () => {
    if (points.length === 0) {
      toast.warning('Add at least one point before saving');
      return;
    }
    onSave(
      points,
      curveName.trim() || 'Unnamed Custom Curve',
      curveDate?.toISOString() ?? null,
      spotCurve === 'Yes'
    );
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>Define Custom Curve</DialogTitle>
      <DialogContent dividers>
        <Stack spacing={3} sx={{ mt: 1 }}>
          <TextField
            label="Curve Name"
            value={curveName}
            onChange={(e) => setCurveName(e.target.value)}
            fullWidth
            size="small"
            placeholder="e.g. My Custom Discount Curve"
          />

          <DatePicker
            label="Curve Date"
            value={curveDate}
            onChange={setCurveDate}
            sx={{ width: '100%' }}
            slotProps={{ textField: { size: 'small' } }}
          />

          <FormControl fullWidth size="small">
            <InputLabel>Spot Curve</InputLabel>
            <Select
              value={spotCurve}
              label="Spot Curve"
              onChange={(e) => setSpotCurve(e.target.value as string)}
            >
              <MenuItem value="Yes">Yes</MenuItem>
              <MenuItem value="No">No</MenuItem>
            </Select>
          </FormControl>

          <Grid container spacing={2} alignItems="flex-end">
            <Grid item xs={3}>
              <TextField
                label="Tenor"
                value={tenor}
                onChange={(e) => setTenor(e.target.value)}
                size="small"
                placeholder="1Y / 6M / 3M / 1D ..."
                fullWidth
              />
            </Grid>
            <Grid item xs={3}>
              <TextField
                label="Rate %"
                value={rate}
                onChange={(e) => setRate(e.target.value)}
                type="number"
                size="small"
                inputProps={{ step: '0.001' }}
                fullWidth
              />
            </Grid>
            <Grid item xs={3}>
              <TextField
                label="StdDev %"
                value={stdDev}
                onChange={(e) => setStdDev(e.target.value)}
                type="number"
                size="small"
                inputProps={{ step: '0.001' }}
                placeholder="0"
                fullWidth
              />
            </Grid>
            <Grid item xs={3}>
              <Button
                variant="outlined"
                startIcon={<AddIcon />}
                onClick={handleAddPoint}
                disabled={!tenor.trim() || !rate.trim()}
                fullWidth
              >
                Add
              </Button>
            </Grid>
          </Grid>

          {points.length > 0 && (
            <Box>
              <Typography variant="subtitle2" gutterBottom>
                Added Points ({points.length})
              </Typography>
              <TableContainer component={Paper} variant="outlined" sx={{ maxHeight: 320 }}>
                <Table size="small" stickyHeader>
                  <TableHead>
                    <TableRow>
                      <TableCell>Tenor</TableCell>
                      <TableCell align="right">Rate %</TableCell>
                      <TableCell align="right">StdDev %</TableCell>
                      <TableCell align="center" width={60}></TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {points.map((point, idx) => (
                      <TableRow key={idx}>
                        <TableCell>{point.tenor}</TableCell>
                        <TableCell align="right">{point.rate}</TableCell>
                        <TableCell align="right">{point.stdDev}</TableCell>
                        <TableCell align="center">
                          <IconButton size="small" color="error" onClick={() => handleRemovePoint(idx)}>
                            <DeleteIcon fontSize="small" />
                          </IconButton>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            </Box>
          )}
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button
          variant="contained"
          startIcon={<SaveIcon />}
          onClick={handleSave}
          disabled={points.length === 0}
        >
          Save Curve
        </Button>
      </DialogActions>
    </Dialog>
  );
};

const TradeForm = () => {
  const [formData, setFormData] = useState({
    desc: '',
    securityId: '',
    sector: '',
    issuer: '',
    cpnrt: '',
    dtMaturity: null as Dayjs | null,
    parAmt: '',
    settlePrc: '',
    dtSettle: dayjs() as Dayjs | null,
    forecastDate: null as Dayjs | null,
    forecastDiscountCurve: '',
    forecastFixingCurve: '',
  });

  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState<any>(null);
  const [searchModalOpen, setSearchModalOpen] = useState(false);

  const [discountModalOpen, setDiscountModalOpen] = useState(false);
  const [fixingModalOpen, setFixingModalOpen] = useState(false);

  const [customDiscountCurve, setCustomDiscountCurve] = useState<{
    name: string;
    date: string | null;
    points: CurvePoint[];
    spotCurve: boolean;
  } | null>(null);

  const [customFixingCurve, setCustomFixingCurve] = useState<{
    name: string;
    date: string | null;
    points: CurvePoint[];
    spotCurve: boolean;
  } | null>(null);

  const handleInputChange = (field: string, value: any) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const handleClear = () => {
    setFormData({
      desc: '',
      securityId: '',
      sector: '',
      issuer: '',
      cpnrt: '',
      dtMaturity: null,
      parAmt: '',
      settlePrc: '',
      dtSettle: dayjs(),
      forecastDate: null,
      forecastDiscountCurve: '',
      forecastFixingCurve: '',
    });
    setCustomDiscountCurve(null);
    setCustomFixingCurve(null);
    setResults(null);
    toast.info('Form cleared');
  };

  const getPayload = () => {
    const basePayload = {
      securityType: 'BOND',
      securityId: formData.securityId || 'BOND001',
      securityDescription: formData.desc,
      sector: formData.sector,
      issuer: formData.issuer,
      coupon: formData.cpnrt ? parseFloat(formData.cpnrt) : undefined,
      maturityDate: formData.dtMaturity?.toISOString(),
      par: formData.parAmt ? parseFloat(formData.parAmt) : undefined,
      price: formData.settlePrc ? parseFloat(formData.settlePrc) : undefined,
      settleDate: formData.dtSettle?.toISOString(),
      forecastDate: formData.forecastDate?.toISOString(),
      forecastDiscountCurve: formData.forecastDiscountCurve,
      forecastFixingCurve: formData.forecastFixingCurve,
    };

    if (customDiscountCurve) {
      (basePayload as any).customDiscountCurve = customDiscountCurve;
    }
    if (customFixingCurve) {
      (basePayload as any).customFixingCurve = customFixingCurve;
    }

    return basePayload;
  };

  const handleCalculate = async () => {
    if (!formData.parAmt || !formData.settlePrc || !formData.dtSettle) {
      toast.error('Please fill in required fields: Par, Price, and Settle Date');
      return;
    }

    setLoading(true);
    try {
      const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3002';
      const response = await axios.post(`${apiUrl}/v1/api/trade/calculate`, getPayload());
      setResults(response.data.data);
      toast.success('Calculation completed!');
    } catch (error: any) {
      console.error('Calculation error:', error);
      toast.error(error.response?.data?.message || 'Calculation failed');
    } finally {
      setLoading(false);
    }
  };

  const handleSecuritySelect = ({ security, details }: any) => {
    setFormData((prev) => ({
      ...prev,
      desc: security.security,
      securityId: security.security,
      sector: security.sector,
      parAmt: security.par.toString(),
      settlePrc: security.price.toString(),
      cpnrt: security.yieldToMaturity.toString(),
    }));

    if (details) {
      setResults(details);
    }

    toast.success(`Selected: ${security.security}`);
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Paper elevation={3} sx={{ p: 4, maxWidth: 1400, mx: 'auto', mt: 4 }}>
        <Stack direction="row" justifyContent="space-between" alignItems="center" mb={3}>
          <Typography variant="h5" fontWeight="bold">
            Trade
          </Typography>
          <Stack direction="row" spacing={2}>
            <Tooltip title="Performs calculation and populates Result Section" arrow>
              <Button
                variant="contained"
                color="primary"
                startIcon={<CalculateIcon />}
                onClick={handleCalculate}
                disabled={loading}
              >
                {loading ? 'Calculating...' : 'Calculate'}
              </Button>
            </Tooltip>
            <Tooltip title="Opens the Search screen" arrow>
              <Button
                variant="contained"
                color="secondary"
                startIcon={<SearchIcon />}
                onClick={() => setSearchModalOpen(true)}
              >
                Search
              </Button>
            </Tooltip>
            <Tooltip title="Sets all parts of the Trade screen to initial values" arrow>
              <Button variant="outlined" startIcon={<ClearIcon />} onClick={handleClear}>
                Clear
              </Button>
            </Tooltip>
          </Stack>
        </Stack>

        <Divider sx={{ mb: 4 }} />

        {/* Security Section */}
        <Box sx={{ mb: 4 }}>
          <Typography variant="h6" fontWeight="bold" sx={{ mb: 2, color: 'primary.main' }}>
            Security
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Security"
                size="small"
                value={formData.desc}
                onChange={(e) => handleInputChange('desc', e.target.value)}
                placeholder="e.g., Air Corp 4.75% 7/1/2028"
              />
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField
                fullWidth
                label="Security ID"
                size="small"
                value={formData.securityId}
                onChange={(e) => handleInputChange('securityId', e.target.value)}
                placeholder="e.g., US2338074F"
              />
            </Grid>
            <Grid item xs={12} md={3}>
              <FormControl fullWidth size="small">
                <InputLabel>Sector</InputLabel>
                <Select
                  value={formData.sector}
                  label="Sector"
                  onChange={(e) => handleInputChange('sector', e.target.value)}
                >
                  <MenuItem value="">Select</MenuItem>
                  <MenuItem value="Corp">Corp</MenuItem>
                  <MenuItem value="Muni">Muni</MenuItem>
                  <MenuItem value="Govt">Govt</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Issuer"
                size="small"
                value={formData.issuer}
                onChange={(e) => handleInputChange('issuer', e.target.value)}
                placeholder="e.g., Air Corp"
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Coupon"
                size="small"
                type="number"
                value={formData.cpnrt}
                onChange={(e) => handleInputChange('cpnrt', e.target.value)}
                placeholder="e.g., 3.500"
                inputProps={{ step: 0.001 }}
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <DatePicker
                label="Maturity"
                sx={{ width: '100%' }}
                slotProps={{ textField: { size: 'small' } }}
                value={formData.dtMaturity}
                onChange={(newValue) => handleInputChange('dtMaturity', newValue)}
              />
            </Grid>
          </Grid>
        </Box>

        {/* Trade Details Section */}
        <Box sx={{ mb: 4 }}>
          <Typography variant="h6" fontWeight="bold" sx={{ mb: 2, color: 'primary.main' }}>
            Trade Details
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Par*"
                size="small"
                type="number"
                value={formData.parAmt}
                onChange={(e) => handleInputChange('parAmt', e.target.value)}
                placeholder="e.g., 100000"
                required
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                label="Price*"
                size="small"
                type="number"
                value={formData.settlePrc}
                onChange={(e) => handleInputChange('settlePrc', e.target.value)}
                placeholder="e.g., 98.125"
                inputProps={{ step: 0.001 }}
                required
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <DatePicker
                label="Settle Date*"
                sx={{ width: '100%' }}
                slotProps={{ textField: { size: 'small', required: true } }}
                value={formData.dtSettle}
                onChange={(newValue) => handleInputChange('dtSettle', newValue)}
              />
            </Grid>
          </Grid>
        </Box>

        {/* Forecast Section */}
        <Box sx={{ mb: 4 }}>
          <Typography variant="h6" fontWeight="bold" sx={{ mb: 2, color: 'primary.main' }}>
            Forecast
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md={4}>
              <DatePicker
                label="Forecast Date"
                sx={{ width: '100%' }}
                slotProps={{ textField: { size: 'small' } }}
                value={formData.forecastDate}
                onChange={(newValue) => handleInputChange('forecastDate', newValue)}
              />
            </Grid>

            <Grid item xs={12} md={4}>
              <FormControl fullWidth size="small">
                <InputLabel>Forecast Discount Curve</InputLabel>
                <Select
                  value={formData.forecastDiscountCurve}
                  label="Forecast Discount Curve"
                  renderValue={(selected) => {
                    if (selected === 'custom' && customDiscountCurve) {
                      return `Other`;
                    }
                    return selected || '';
                  }}
                  onChange={(e) => {
                    const val = e.target.value as string;
                    if (val === 'Other') {
                      setDiscountModalOpen(true);
                    } else {
                      handleInputChange('forecastDiscountCurve', val);
                      if (customDiscountCurve && val !== 'custom') {
                        setCustomDiscountCurve(null);
                      }
                    }
                  }}
                >
                  <MenuItem value="">Select</MenuItem>
                  <MenuItem value="SOFR">SOFR</MenuItem>
                  <MenuItem value="Forward">Forward</MenuItem>
                  <MenuItem value="Other">Other...</MenuItem>
                  <MenuItem value="custom" sx={{ display: 'none' }} />
                </Select>
              </FormControl>
            </Grid>

            <Grid item xs={12} md={4}>
              <FormControl fullWidth size="small">
                <InputLabel>Forecast Fixing Curve</InputLabel>
                <Select
                  value={formData.forecastFixingCurve}
                  label="Forecast Fixing Curve"
                  renderValue={(selected) => {
                    if (selected === 'custom' && customFixingCurve) {
                      return `Other`;
                    }
                    return selected || '';
                  }}
                  onChange={(e) => {
                    const val = e.target.value as string;
                    if (val === 'Other') {
                      setFixingModalOpen(true);
                    } else {
                      handleInputChange('forecastFixingCurve', val);
                      if (customFixingCurve && val !== 'custom') {
                        setCustomFixingCurve(null);
                      }
                    }
                  }}
                >
                  <MenuItem value="">Select</MenuItem>
                  <MenuItem value="SOFR">SOFR</MenuItem>
                  <MenuItem value="Forward">Forward</MenuItem>
                  <MenuItem value="Other">Other...</MenuItem>
                  <MenuItem value="custom" sx={{ display: 'none' }} />
                </Select>
              </FormControl>
            </Grid>
          </Grid>
        </Box>

        {results && <ResultSections data={results} />}
      </Paper>

      <CustomCurveModal
        open={discountModalOpen}
        onClose={() => setDiscountModalOpen(false)}
        onSave={(points, name, date, spot) => {
          setCustomDiscountCurve({ name, date, points, spotCurve: spot });
          handleInputChange('forecastDiscountCurve', 'custom');
          toast.success('Custom discount curve saved');
        }}
      />

      <CustomCurveModal
        open={fixingModalOpen}
        onClose={() => setFixingModalOpen(false)}
        onSave={(points, name, date, spot) => {
          setCustomFixingCurve({ name, date, points, spotCurve: spot });
          handleInputChange('forecastFixingCurve', 'custom');
          toast.success('Custom fixing curve saved');
        }}
      />

      <SearchModal
        open={searchModalOpen}
        onClose={() => setSearchModalOpen(false)}
        onSecuritySelect={handleSecuritySelect}
      />
    </LocalizationProvider>
  );
};

export default TradeForm;