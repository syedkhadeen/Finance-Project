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
    Tooltip
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
import SearchModal from './SearchModal';
import ResultSections from './ResultSections';

const TradeForm = () => {
    const [formData, setFormData] = useState({
        // Security Section
        desc: '',                    // Security description
        securityId: '',              // Security ID (IBM in example)
        sector: '',                  // Sector
        issuer: '',                  // Issuer
        cpnrt: '',                   // Coupon rate
        dtMaturity: null as Dayjs | null,  // Maturity date
        
        // Trade Details
        parAmt: '',                  // Par amount
        settlePrc: '',               // Price (settle price)
        dtSettle: dayjs() as Dayjs | null,  // Settle date
        
        // Forecast
        forecastDate: null as Dayjs | null,
        forecastDiscountCurve: '',
        forecastFixingCurve: '',
    });

    const [loading, setLoading] = useState(false);
    const [results, setResults] = useState<any>(null);
    const [searchModalOpen, setSearchModalOpen] = useState(false);

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
        setResults(null);
        toast.info('Form cleared');
    };

    const getPayload = () => {
        return {
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
        };
    };

    const handleCalculate = async () => {
        // Validation
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
        // Populate form with selected security
        setFormData((prev) => ({
            ...prev,
            desc: security.security,
            securityId: security.security,
            sector: security.sector,
            parAmt: security.par.toString(),
            settlePrc: security.price.toString(),
            cpnrt: security.yieldToMaturity.toString(),
        }));
        
        // Set results if details are provided
        if (details) {
            setResults(details);
        }
        
        toast.success(`Selected: ${security.security}`);
    };

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Paper elevation={3} sx={{ p: 4, maxWidth: 1400, mx: 'auto', mt: 4 }}>
                {/* Header with Action Buttons */}
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
                            <Button 
                                variant="outlined" 
                                startIcon={<ClearIcon />}
                                onClick={handleClear}
                            >
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
                            <Tooltip title="Enter or select the security description" arrow placement="top">
                                <TextField
                                    fullWidth
                                    label="Security"
                                    size="small"
                                    value={formData.desc}
                                    onChange={(e) => handleInputChange('desc', e.target.value)}
                                    placeholder="e.g., Air Corp 4.75% 7/1/2028"
                                />
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={3}>
                            <Tooltip title="Enter the unique security identifier" arrow placement="top">
                                <TextField
                                    fullWidth
                                    label="Security ID"
                                    size="small"
                                    value={formData.securityId}
                                    onChange={(e) => handleInputChange('securityId', e.target.value)}
                                    placeholder="e.g., US2338074F"
                                />
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={3}>
                            <Tooltip title="Select the sector type (Corp, Govt, Muni)" arrow placement="top">
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
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Tooltip title="Enter the issuer name" arrow placement="top">
                                <TextField
                                    fullWidth
                                    label="Issuer"
                                    size="small"
                                    value={formData.issuer}
                                    onChange={(e) => handleInputChange('issuer', e.target.value)}
                                    placeholder="e.g., Air Corp"
                                />
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Tooltip title="Enter the coupon rate (annual %)" arrow placement="top">
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
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Tooltip title="Select the maturity date" arrow placement="top">
                                <DatePicker
                                    label="Maturity"
                                    sx={{ width: '100%' }}
                                    slotProps={{ textField: { size: 'small' } }}
                                    value={formData.dtMaturity}
                                    onChange={(newValue) => handleInputChange('dtMaturity', newValue)}
                                />
                            </Tooltip>
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
                            <Tooltip title="Enter the par amount (face value)" arrow placement="top">
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
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Tooltip title="Enter the settlement price" arrow placement="top">
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
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Tooltip title="Select the settlement date" arrow placement="top">
                                <DatePicker
                                    label="Settle Date*"
                                    sx={{ width: '100%' }}
                                    slotProps={{ textField: { size: 'small', required: true } }}
                                    value={formData.dtSettle}
                                    onChange={(newValue) => handleInputChange('dtSettle', newValue)}
                                />
                            </Tooltip>
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
                            <Tooltip title="Select the forecast date for future value calculations" arrow placement="top">
                                <DatePicker
                                    label="Forecast Date"
                                    sx={{ width: '100%' }}
                                    slotProps={{ textField: { size: 'small' } }}
                                    value={formData.forecastDate}
                                    onChange={(newValue) => handleInputChange('forecastDate', newValue)}
                                />
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Tooltip title="Select the discount curve for forecast calculations" arrow placement="top">
                                <FormControl fullWidth size="small">
                                    <InputLabel>Forecast Discount Curve</InputLabel>
                                    <Select
                                        value={formData.forecastDiscountCurve}
                                        label="Forecast Discount Curve"
                                        onChange={(e) => handleInputChange('forecastDiscountCurve', e.target.value)}
                                    >
                                        <MenuItem value="">Select</MenuItem>
                                        <MenuItem value="Forward">Forward</MenuItem>
                                        <MenuItem value="SOFR">SOFR</MenuItem>
                                    </Select>
                                </FormControl>
                            </Tooltip>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Tooltip title="Select the fixing curve for forecast calculations" arrow placement="top">
                                <FormControl fullWidth size="small">
                                    <InputLabel>Forecast Fixing Curve</InputLabel>
                                    <Select
                                        value={formData.forecastFixingCurve}
                                        label="Forecast Fixing Curve"
                                        onChange={(e) => handleInputChange('forecastFixingCurve', e.target.value)}
                                    >
                                        <MenuItem value="">Select</MenuItem>
                                        <MenuItem value="SOFR">SOFR</MenuItem>
                                        <MenuItem value="Forward">Forward</MenuItem>
                                    </Select>
                                </FormControl>
                            </Tooltip>
                        </Grid>
                    </Grid>
                </Box>

                {/* Results Section */}
                {results && <ResultSections data={results} />}
            </Paper>

            {/* Search Modal */}
            <SearchModal
                open={searchModalOpen}
                onClose={() => setSearchModalOpen(false)}
                onSecuritySelect={handleSecuritySelect}
            />
        </LocalizationProvider>
    );
};

export default TradeForm;
