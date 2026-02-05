import React, { useState } from 'react';
import {
    Box,
    Typography,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Grid,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

interface ResultSectionsProps {
    data: any;
}

const ResultSections: React.FC<ResultSectionsProps> = ({ data }) => {
    const [expanded, setExpanded] = useState<string[]>(['analytics', 'cashflow', 'risk', 'projection', 'keyRate']);

    const handleAccordionChange = (panel: string) => {
        setExpanded((prev) =>
            prev.includes(panel)
                ? prev.filter((p) => p !== panel)
                : [...prev, panel]
        );
    };

    if (!data) return null;

    const { analytics, cashflow, keyRateDuration } = data;

    return (
        <Box sx={{ mt: 4 }}>
            {/* Analytics Section */}
            <Accordion 
                expanded={expanded.includes('analytics')}
                onChange={() => handleAccordionChange('analytics')}
                sx={{ mb: 2 }}
            >
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    sx={{ 
                        backgroundColor: '#e3f2fd',
                        '&:hover': { backgroundColor: '#bbdefb' }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold">
                        Analytics
                    </Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container spacing={2}>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Principal</Typography>
                                <Typography variant="h6">${analytics?.principal?.toLocaleString()}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Accrued</Typography>
                                <Typography variant="h6">${analytics?.accrued?.toLocaleString()}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Payment</Typography>
                                <Typography variant="h6">${analytics?.netPayment?.toLocaleString()}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Price</Typography>
                                <Typography variant="h6">{analytics?.price}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Yield to Maturity</Typography>
                                <Typography variant="h6">{analytics?.yieldToMaturity}%</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Yield to Worst</Typography>
                                <Typography variant="h6">{analytics?.yieldToWorst}%</Typography>
                            </Paper>
                        </Grid>
                    </Grid>
                </AccordionDetails>
            </Accordion>

            {/* Risk Section */}
            <Accordion 
                expanded={expanded.includes('risk')}
                onChange={() => handleAccordionChange('risk')}
                sx={{ mb: 2 }}
            >
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    sx={{ 
                        backgroundColor: '#fff3e0',
                        '&:hover': { backgroundColor: '#ffe0b2' }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold">
                        Risk
                    </Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container spacing={2}>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Mod Duration</Typography>
                                <Typography variant="h6">{analytics?.duration?.toFixed(3)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Convexity</Typography>
                                <Typography variant="h6">{analytics?.convexity?.toFixed(4)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Maturity Length</Typography>
                                <Typography variant="h6">{analytics?.maturityYears?.toFixed(2)} years</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">OAS Spread (bp)</Typography>
                                <Typography variant="h6">{analytics?.zSpread?.toFixed(2)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">OAS Duration(yr)</Typography>
                                <Typography variant="h6">{analytics?.duration?.toFixed(2)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Oas Convexity(yr*yr)</Typography>
                                <Typography variant="h6">{analytics?.convexity?.toFixed(2)}</Typography>
                            </Paper>
                        </Grid>
                    </Grid>
                </AccordionDetails>
            </Accordion>

            {/* Projection Section */}
            <Accordion 
                expanded={expanded.includes('projection')}
                onChange={() => handleAccordionChange('projection')}
                sx={{ mb: 2 }}
            >
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    sx={{ 
                        backgroundColor: '#e8f5e9',
                        '&:hover': { backgroundColor: '#c8e6c9' }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold">
                        Projection
                    </Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container spacing={2}>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Fair Value</Typography>
                                <Typography variant="h6">${analytics?.fairValue?.toLocaleString()}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Z-Spread</Typography>
                                <Typography variant="h6">{analytics?.zSpread?.toFixed(2)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Generated Cash</Typography>
                                <Typography variant="h6">$0.00</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">PnL Carry</Typography>
                                <Typography variant="h6">$0.00</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">PnlRoll</Typography>
                                <Typography variant="h6">$0.00</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">PnlFinance</Typography>
                                <Typography variant="h6">$0.00</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Future Return</Typography>
                                <Typography variant="h6">{analytics?.futureReturn}%</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Upside Future Return</Typography>
                                <Typography variant="h6">{analytics?.futureReturnUpSide}%</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">Down Side Return</Typography>
                                <Typography variant="h6">{analytics?.futureReturnDnSide}%</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">DV01-Parallel</Typography>
                                <Typography variant="h6">0</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">DV01-2x5s</Typography>
                                <Typography variant="h6">0</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={0} sx={{ p: 2, backgroundColor: '#f5f5f5' }}>
                                <Typography variant="caption" color="text.secondary">DV01-5s10s</Typography>
                                <Typography variant="h6">0</Typography>
                            </Paper>
                        </Grid>
                    </Grid>
                </AccordionDetails>
            </Accordion>

            {/* Cashflow Section */}
            <Accordion 
                expanded={expanded.includes('cashflow')}
                onChange={() => handleAccordionChange('cashflow')}
                sx={{ mb: 2 }}
            >
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    sx={{ 
                        backgroundColor: '#f3e5f5',
                        '&:hover': { backgroundColor: '#e1bee7' }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold">
                        Cashflow
                    </Typography>
                </AccordionSummary>
                <AccordionDetails>
                    {cashflow && cashflow.length > 0 ? (
                        <TableContainer component={Paper} variant="outlined">
                            <Table size="small">
                                <TableHead sx={{ backgroundColor: '#f5f5f5' }}>
                                    <TableRow>
                                        <TableCell align="center">Date</TableCell>
                                        <TableCell align="center">DayOfWk</TableCell>
                                        <TableCell align="center">Periods</TableCell>
                                        <TableCell align="center">Years</TableCell>
                                        <TableCell align="center">Total Cash</TableCell>
                                        <TableCell align="center">Total Prin</TableCell>
                                        <TableCell align="center">Sched Prin</TableCell>
                                        <TableCell align="center">Interest</TableCell>
                                        <TableCell align="center">Fed taxes</TableCell>
                                        <TableCell align="center">State tax</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {cashflow.map((row: any, index: number) => (
                                        <TableRow key={index}>
                                            <TableCell align="center">{row.date}</TableCell>
                                            <TableCell align="center">{row.dayOfWeek}</TableCell>
                                            <TableCell align="center">{row.periods.toFixed(4)}</TableCell>
                                            <TableCell align="center">{row.years.toFixed(3)}</TableCell>
                                            <TableCell align="center">{row.totalCash.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                                            <TableCell align="center">{row.totalPrin.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                                            <TableCell align="center">{row.schedPrin.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                                            <TableCell align="center">{row.interest.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                                            <TableCell align="center">{row.fedTaxes.toFixed(2)}</TableCell>
                                            <TableCell align="center">{row.stateTax.toFixed(2)}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    ) : (
                        <Typography color="text.secondary">No cashflow data available</Typography>
                    )}
                </AccordionDetails>
            </Accordion>

            {/* Key Rate Duration Section */}
            <Accordion 
                expanded={expanded.includes('keyRate')}
                onChange={() => handleAccordionChange('keyRate')}
                sx={{ mb: 2 }}
            >
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    sx={{ 
                        backgroundColor: '#fce4ec',
                        '&:hover': { backgroundColor: '#f8bbd0' }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold">
                        Key Rate Duration
                    </Typography>
                </AccordionSummary>
                <AccordionDetails>
                    {keyRateDuration && keyRateDuration.length > 0 ? (
                        <TableContainer component={Paper} variant="outlined">
                            <Table size="small">
                                <TableHead sx={{ backgroundColor: '#f5f5f5' }}>
                                    <TableRow>
                                        <TableCell align="center">Key Rate Term</TableCell>
                                        <TableCell align="center">Key Rate(%)</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {keyRateDuration.map((row: any, index: number) => (
                                        <TableRow key={index}>
                                            <TableCell align="center">{row.tenor}</TableCell>
                                            <TableCell align="center">{row.duration}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    ) : (
                        <Typography color="text.secondary">No key rate duration data available</Typography>
                    )}
                </AccordionDetails>
            </Accordion>
        </Box>
    );
};

export default ResultSections;
