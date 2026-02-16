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
                        backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(91, 159, 237, 0.15)' : '#e3f2fd',
                        '&:hover': { 
                            backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(91, 159, 237, 0.25)' : '#bbdefb'
                        }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold" color="text.primary">
                        Analytics
                    </Typography>
                </AccordionSummary>
                <AccordionDetails sx={{ backgroundColor: 'background.default' }}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Principal</Typography>
                                <Typography variant="h6" color="text.primary">${analytics?.principal?.toLocaleString()}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Accrued</Typography>
                                <Typography variant="h6" color="text.primary">${analytics?.accrued?.toLocaleString()}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Payment</Typography>
                                <Typography variant="h6" color="text.primary">${analytics?.netPayment?.toLocaleString()}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Price</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.price}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Yield to Maturity</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.yieldToMaturity}%</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Yield to Worst</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.yieldToWorst}%</Typography>
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
                        backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(255, 197, 61, 0.15)' : '#fff3e0',
                        '&:hover': { 
                            backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(255, 197, 61, 0.25)' : '#ffe0b2'
                        }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold" color="text.primary">
                        Risk
                    </Typography>
                </AccordionSummary>
                <AccordionDetails sx={{ backgroundColor: 'background.default' }}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Mod Duration</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.duration?.toFixed(3)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Convexity</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.convexity?.toFixed(4)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Maturity Length</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.maturityYears?.toFixed(2)} years</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">OAS Spread (bp)</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.zSpread?.toFixed(2)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">OAS Duration(yr)</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.duration?.toFixed(2)}</Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Paper 
                                elevation={0} 
                                sx={{ 
                                    p: 2, 
                                    backgroundColor: 'background.paper',
                                    border: '1px solid',
                                    borderColor: 'divider'
                                }}
                            >
                                <Typography variant="caption" color="text.secondary">Oas Convexity(yr*yr)</Typography>
                                <Typography variant="h6" color="text.primary">{analytics?.convexity?.toFixed(2)}</Typography>
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
                        backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(115, 209, 61, 0.15)' : '#e8f5e9',
                        '&:hover': { 
                            backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(115, 209, 61, 0.25)' : '#c8e6c9'
                        }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold" color="text.primary">
                        Projection
                    </Typography>
                </AccordionSummary>
                <AccordionDetails sx={{ backgroundColor: 'background.default' }}>
                    <Grid container spacing={2}>
                        {[
                            { label: 'Fair Value', value: `$${analytics?.fairValue?.toLocaleString()}` },
                            { label: 'Z-Spread', value: analytics?.zSpread?.toFixed(2) },
                            { label: 'Generated Cash', value: '$0.00' },
                            { label: 'PnL Carry', value: '$0.00' },
                            { label: 'PnlRoll', value: '$0.00' },
                            { label: 'PnlFinance', value: '$0.00' },
                            { label: 'Future Return', value: `${analytics?.futureReturn}%` },
                            { label: 'Upside Future Return', value: `${analytics?.futureReturnUpSide}%` },
                            { label: 'Down Side Return', value: `${analytics?.futureReturnDnSide}%` },
                            { label: 'DV01-Parallel', value: '0' },
                            { label: 'DV01-2x5s', value: '0' },
                            { label: 'DV01-5s10s', value: '0' }
                        ].map((item, idx) => (
                            <Grid item xs={12} md={4} key={idx}>
                                <Paper 
                                    elevation={0} 
                                    sx={{ 
                                        p: 2, 
                                        backgroundColor: 'background.paper',
                                        border: '1px solid',
                                        borderColor: 'divider'
                                    }}
                                >
                                    <Typography variant="caption" color="text.secondary">{item.label}</Typography>
                                    <Typography variant="h6" color="text.primary">{item.value}</Typography>
                                </Paper>
                            </Grid>
                        ))}
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
                        backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(109, 123, 206, 0.15)' : '#f3e5f5',
                        '&:hover': { 
                            backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(109, 123, 206, 0.25)' : '#e1bee7'
                        }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold" color="text.primary">
                        Cashflow
                    </Typography>
                </AccordionSummary>
                <AccordionDetails sx={{ backgroundColor: 'background.default' }}>
                    {cashflow && cashflow.length > 0 ? (
                        <TableContainer 
                            component={Paper} 
                            variant="outlined"
                            sx={{ backgroundColor: 'background.paper' }}
                        >
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Date</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>DayOfWk</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Periods</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Years</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Total Cash</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Total Prin</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Sched Prin</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Interest</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Fed taxes</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>State tax</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {cashflow.map((row: any, index: number) => (
                                        <TableRow key={index} hover>
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
                        backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(255, 77, 79, 0.15)' : '#fce4ec',
                        '&:hover': { 
                            backgroundColor: (theme) => theme.palette.mode === 'dark' ? 'rgba(255, 77, 79, 0.25)' : '#f8bbd0'
                        }
                    }}
                >
                    <Typography variant="h6" fontWeight="bold" color="text.primary">
                        Key Rate Duration
                    </Typography>
                </AccordionSummary>
                <AccordionDetails sx={{ backgroundColor: 'background.default' }}>
                    {keyRateDuration && keyRateDuration.length > 0 ? (
                        <TableContainer 
                            component={Paper} 
                            variant="outlined"
                            sx={{ backgroundColor: 'background.paper' }}
                        >
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Key Rate Term</TableCell>
                                        <TableCell align="center" sx={{ backgroundColor: 'background.default', fontWeight: 600 }}>Key Rate(%)</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {keyRateDuration.map((row: any, index: number) => (
                                        <TableRow key={index} hover>
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
