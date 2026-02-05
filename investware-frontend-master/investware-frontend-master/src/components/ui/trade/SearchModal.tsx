import React, { useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    TextField,
    Grid,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Box,
    Typography,
    Tooltip,
    IconButton,
    CircularProgress
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import SearchIcon from '@mui/icons-material/Search';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { Dayjs } from 'dayjs';
import axios from 'axios';
import { toast } from 'react-toastify';
import SearchResults from './SearchResults';

interface SearchModalProps {
    open: boolean;
    onClose: () => void;
    onSecuritySelect: (security: any) => void;
}

const SearchModal: React.FC<SearchModalProps> = ({ open, onClose, onSecuritySelect }) => {
    const [searchCriteria, setSearchCriteria] = useState({
        sector: '',
        couponLow: '',
        couponHigh: '',
        maturityLow: null as Dayjs | null,
        maturityHigh: null as Dayjs | null,
    });

    const [loading, setLoading] = useState(false);
    const [searchResults, setSearchResults] = useState<any[]>([]);

    const handleInputChange = (field: string, value: any) => {
        setSearchCriteria((prev) => ({ ...prev, [field]: value }));
    };

    const handleSearch = async () => {
        setLoading(true);
        try {
            const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3002';
            
            // Build search criteria
            const criteria: any = {};
            
            if (searchCriteria.sector) criteria.sector = searchCriteria.sector;
            if (searchCriteria.couponLow) criteria.couponFrom = parseFloat(searchCriteria.couponLow);
            if (searchCriteria.couponHigh) criteria.couponTo = parseFloat(searchCriteria.couponHigh);
            if (searchCriteria.maturityLow) criteria.maturityFrom = searchCriteria.maturityLow.toISOString();
            if (searchCriteria.maturityHigh) criteria.maturityTo = searchCriteria.maturityHigh.toISOString();
            
            const response = await axios.post(`${apiUrl}/v1/api/trade/search`, criteria);
            
            setSearchResults(response.data.data || []);
            toast.success(`Found ${response.data.data?.length || 0} securities`);
        } catch (error: any) {
            console.error('Search error:', error);
            toast.error(error.response?.data?.message || 'Search failed');
            setSearchResults([]);
        } finally {
            setLoading(false);
        }
    };

    const handleSecuritySelect = (security: any, details: any) => {
        onSecuritySelect({ security, details });
        onClose();
    };

    const handleClear = () => {
        setSearchCriteria({
            sector: '',
            couponLow: '',
            couponHigh: '',
            maturityLow: null,
            maturityHigh: null,
        });
        setSearchResults([]);
    };

    return (
        <Dialog 
            open={open} 
            onClose={onClose}
            maxWidth="xl"
            fullWidth
            PaperProps={{
                sx: { minHeight: '80vh' }
            }}
        >
            <DialogTitle sx={{ 
                backgroundColor: '#1976d2', 
                color: 'white',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center'
            }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <SearchIcon />
                    <Typography variant="h6">Security Search</Typography>
                </Box>
                <IconButton
                    edge="end"
                    color="inherit"
                    onClick={onClose}
                    aria-label="close"
                >
                    <CloseIcon />
                </IconButton>
            </DialogTitle>

            <DialogContent dividers>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                    {/* Search Filters */}
                    <Box sx={{ mb: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
                        <Typography variant="subtitle1" fontWeight="bold" sx={{ mb: 2 }}>
                            Search Filters
                        </Typography>
                        <Grid container spacing={2}>
                            <Grid item xs={12} md={4}>
                                <Tooltip title="Select the sector type to filter securities" arrow>
                                    <FormControl fullWidth size="small">
                                        <InputLabel>Sector</InputLabel>
                                        <Select
                                            value={searchCriteria.sector}
                                            label="Sector"
                                            onChange={(e) => handleInputChange('sector', e.target.value)}
                                        >
                                            <MenuItem value="">All</MenuItem>
                                            <MenuItem value="Corp">Corp</MenuItem>
                                            <MenuItem value="Muni">Muni</MenuItem>
                                            <MenuItem value="Govt">Govt</MenuItem>
                                        </Select>
                                    </FormControl>
                                </Tooltip>
                            </Grid>

                            <Grid item xs={12} md={4}>
                                <Tooltip title="Enter minimum coupon rate" arrow>
                                    <TextField
                                        fullWidth
                                        label="Coupon Low"
                                        size="small"
                                        type="number"
                                        value={searchCriteria.couponLow}
                                        onChange={(e) => handleInputChange('couponLow', e.target.value)}
                                        inputProps={{ step: 0.01 }}
                                    />
                                </Tooltip>
                            </Grid>

                            <Grid item xs={12} md={4}>
                                <Tooltip title="Enter maximum coupon rate" arrow>
                                    <TextField
                                        fullWidth
                                        label="Coupon High"
                                        size="small"
                                        type="number"
                                        value={searchCriteria.couponHigh}
                                        onChange={(e) => handleInputChange('couponHigh', e.target.value)}
                                        inputProps={{ step: 0.01 }}
                                    />
                                </Tooltip>
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <Tooltip title="Select earliest maturity date" arrow>
                                    <DatePicker
                                        label="Maturity Low"
                                        sx={{ width: '100%' }}
                                        slotProps={{ textField: { size: 'small' } }}
                                        value={searchCriteria.maturityLow}
                                        onChange={(newValue) => handleInputChange('maturityLow', newValue)}
                                    />
                                </Tooltip>
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <Tooltip title="Select latest maturity date" arrow>
                                    <DatePicker
                                        label="Maturity High"
                                        sx={{ width: '100%' }}
                                        slotProps={{ textField: { size: 'small' } }}
                                        value={searchCriteria.maturityHigh}
                                        onChange={(newValue) => handleInputChange('maturityHigh', newValue)}
                                    />
                                </Tooltip>
                            </Grid>
                        </Grid>
                    </Box>

                    {/* Search Results */}
                    {searchResults.length > 0 && (
                        <SearchResults 
                            data={searchResults} 
                            onSecuritySelect={handleSecuritySelect}
                        />
                    )}

                    {searchResults.length === 0 && !loading && (
                        <Box sx={{ 
                            textAlign: 'center', 
                            py: 8,
                            color: 'text.secondary'
                        }}>
                            <SearchIcon sx={{ fontSize: 64, opacity: 0.3, mb: 2 }} />
                            <Typography variant="h6">
                                Enter search criteria and click Search
                            </Typography>
                        </Box>
                    )}

                    {loading && (
                        <Box sx={{ textAlign: 'center', py: 8 }}>
                            <CircularProgress />
                            <Typography variant="body1" sx={{ mt: 2 }}>
                                Searching securities...
                            </Typography>
                        </Box>
                    )}
                </LocalizationProvider>
            </DialogContent>

            <DialogActions sx={{ p: 2, gap: 1 }}>
                <Button 
                    onClick={handleClear} 
                    variant="outlined"
                    disabled={loading}
                >
                    Clear
                </Button>
                <Button 
                    onClick={handleSearch} 
                    variant="contained"
                    startIcon={loading ? <CircularProgress size={20} /> : <SearchIcon />}
                    disabled={loading}
                >
                    {loading ? 'Searching...' : 'Search'}
                </Button>
                <Button 
                    onClick={onClose} 
                    variant="outlined"
                    color="secondary"
                >
                    Close
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default SearchModal;
