import React, { useState } from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Typography,
    Box,
    Checkbox,
    CircularProgress
} from '@mui/material';
import axios from 'axios';
import { toast } from 'react-toastify';

interface SecurityResult {
    selected: boolean;
    security: string;
    sector: string;
    par: number;
    price: number;
    accruedInterest: number;
    yieldToMaturity: number;
    yieldToWorst: number;
    marketValue: number;
    maturityLength: number;
    duration: number;
    convexity: number;
    fairValue: number;
    zSpread: number;
    forecastReturn: number;
    forecastUpSide: number;
    forecastDownSide: number;
}

interface SearchResultsProps {
    data: SecurityResult[];
    onSecuritySelect: (security: SecurityResult, details: any) => void;
}

const SearchResults: React.FC<SearchResultsProps> = ({ data, onSecuritySelect }) => {
    const [selectedSecurity, setSelectedSecurity] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    if (!data || data.length === 0) return null;

    const handleCheckboxChange = async (security: SecurityResult) => {
        // If clicking the same checkbox, unselect it
        if (selectedSecurity === security.security) {
            setSelectedSecurity(null);
            onSecuritySelect(security, null);
            return;
        }

        setLoading(true);
        setSelectedSecurity(security.security);

        try {
            const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3002';
            
            // Calculate details for the selected security
            const response = await axios.post(`${apiUrl}/v1/api/trade/calculate`, {
                securityType: 'BOND',
                securityId: security.security,
                securityDescription: security.security,
                sector: security.sector,
                par: security.par,
                price: security.price,
                yield: security.yieldToMaturity,
                settleDate: new Date().toISOString(),
                maturityDate: new Date(Date.now() + security.maturityLength * 365 * 24 * 60 * 60 * 1000).toISOString(),
                coupon: security.yieldToMaturity * 0.9, // Approximate coupon
            });

            onSecuritySelect(security, response.data.data);
            toast.success(`Loaded details for ${security.security}`);
        } catch (error: any) {
            console.error('Error loading security details:', error);
            toast.error('Failed to load security details');
            setSelectedSecurity(null);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Box sx={{ mt: 4 }}>
            <Typography 
                variant="h6" 
                fontWeight="bold" 
                sx={{ 
                    backgroundColor: 'primary.main', 
                    color: 'white',
                    p: 1.5, 
                    borderRadius: '4px 4px 0 0' 
                }}
            >
                Search Results ({data.length} securities found)
            </Typography>
            <TableContainer 
                component={Paper} 
                variant="outlined" 
                sx={{ 
                    borderRadius: '0 0 4px 4px', 
                    borderTop: 'none',
                    maxHeight: 400,
                    overflow: 'auto',
                    backgroundColor: 'background.paper'
                }}
            >
                <Table size="small" stickyHeader>
                    <TableHead>
                        <TableRow>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>
                                {loading ? <CircularProgress size={20} /> : 'Select'}
                            </TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Security</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Sector</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Par</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Price</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Accrued Interest</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Yield-to-Maturity</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Yield-to-Worst</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Market Value</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Maturity Length</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Duration</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Convexity</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Fair Value</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Z-Spread</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Forecast Return</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Forecast UpSide</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold', backgroundColor: 'background.default' }}>Forecast DownSide</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((row, index) => (
                            <TableRow 
                                key={index}
                                hover
                                sx={{ 
                                    backgroundColor: selectedSecurity === row.security ? (theme) => 
                                        theme.palette.mode === 'dark' ? 'rgba(91, 159, 237, 0.15)' : '#e3f2fd' 
                                        : 'inherit'
                                }}
                            >
                                <TableCell align="center">
                                    <Checkbox
                                        checked={selectedSecurity === row.security}
                                        onChange={() => handleCheckboxChange(row)}
                                        disabled={loading}
                                        color="primary"
                                    />
                                </TableCell>
                                <TableCell align="center">{row.security}</TableCell>
                                <TableCell align="center">{row.sector}</TableCell>
                                <TableCell align="center">{row.par.toLocaleString()}</TableCell>
                                <TableCell align="center">{row.price.toFixed(2)}</TableCell>
                                <TableCell align="center">{row.accruedInterest.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                                <TableCell align="center">{row.yieldToMaturity.toFixed(3)}</TableCell>
                                <TableCell align="center">{row.yieldToWorst.toFixed(3)}</TableCell>
                                <TableCell align="center">{row.marketValue.toLocaleString()}</TableCell>
                                <TableCell align="center">{row.maturityLength.toFixed(2)}</TableCell>
                                <TableCell align="center">{row.duration.toFixed(2)}</TableCell>
                                <TableCell align="center">{row.convexity.toFixed(2)}</TableCell>
                                <TableCell align="center">{row.fairValue.toLocaleString()}</TableCell>
                                <TableCell align="center">{row.zSpread.toFixed(2)}</TableCell>
                                <TableCell align="center">{row.forecastReturn.toFixed(2)}</TableCell>
                                <TableCell align="center">{row.forecastUpSide.toFixed(2)}</TableCell>
                                <TableCell align="center">{row.forecastDownSide.toFixed(2)}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            {selectedSecurity && (
                <Typography 
                    variant="body2" 
                    sx={{ mt: 1, color: 'primary.main', fontStyle: 'italic' }}
                >
                    Selected: {selectedSecurity} - Detailed analytics displayed below
                </Typography>
            )}
        </Box>
    );
};

export default SearchResults;
