import React from 'react';
import { Grid, Typography, Paper, Divider, Box } from '@mui/material';

interface ResultAnalyticsProps {
    data: any;
}

const ResultAnalytics: React.FC<ResultAnalyticsProps> = ({ data }) => {
    if (!data) return null;

    const renderCell = (label: string, value: any) => (
        <Grid item xs={3} sx={{ border: '1px solid #ccc', p: 1 }}>
            <Typography variant="caption" color="textSecondary" display="block" textAlign="center">
                {label}
            </Typography>
            <Typography variant="body2" fontWeight="bold" textAlign="center">
                {value ?? '-'}
            </Typography>
        </Grid>
    );

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h6" fontWeight="bold" sx={{ backgroundColor: '#e0e0e0', p: 1, borderRadius: '4px 4px 0 0' }}>
                Results
            </Typography>
            <Paper variant="outlined" sx={{ borderRadius: '0 0 4px 4px', borderTop: 'none' }}>
                <Grid container>
                    {renderCell('Principal', data.principal)}
                    {renderCell('Accrued Days', data.accruedDays)}
                    {renderCell('Duration', data.duration)}
                    {renderCell('Convexity', data.convexity)}

                    {renderCell('Accrued', data.accrued)}
                    {renderCell('Price', data.price)}
                    {renderCell('Yield to Maturity', data.yieldToMaturity)}
                    {renderCell('Yield to Worst', data.yieldToWorst)}

                    {renderCell('Net Payment', data.netPayment)}
                    {renderCell('Maturity (yrs)', data.maturityYears)}
                    {renderCell('Last Payment', data.lastPayment)}
                    {renderCell('Next Payment', data.nextPayment)}

                    {renderCell('Fair Value', data.fairValue)}
                    {renderCell('Z-Spread', data.zSpread)}
                    {renderCell('Future Value', data.futureValue)}
                    {renderCell('Current Value', data.currentValue)}

                    {renderCell('Future Date', data.futureDate)}
                    {renderCell('Future Return', data.futureReturn)}
                    {renderCell('Future Return UpSide', data.futureReturnUpSide)}
                    {renderCell('Future Return DnSide', data.futureReturnDnSide)}
                </Grid>
            </Paper>
        </Box>
    );
};

export default ResultAnalytics;
