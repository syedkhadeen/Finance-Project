import React from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Typography,
    Box
} from '@mui/material';

interface CashflowRow {
    date: string;
    dayOfWeek: string;
    periods: number;
    years: number;
    totalCash: number;
    totalPrin: number;
    schedPrin: number;
    interest: number;
    fedTaxes: number;
    stateTax: number;
}

interface ResultCashflowProps {
    data: CashflowRow[];
}

const ResultCashflow: React.FC<ResultCashflowProps> = ({ data }) => {
    if (!data || data.length === 0) return null;

    const totals = data.reduce((acc, row) => ({
        periods: acc.periods + (row.periods || 0),
        years: acc.years + (row.years || 0),
        totalCash: acc.totalCash + (row.totalCash || 0),
        totalPrin: acc.totalPrin + (row.totalPrin || 0),
        schedPrin: acc.schedPrin + (row.schedPrin || 0),
        interest: acc.interest + (row.interest || 0),
        fedTaxes: acc.fedTaxes + (row.fedTaxes || 0),
        stateTax: acc.stateTax + (row.stateTax || 0),
    }), { periods: 0, years: 0, totalCash: 0, totalPrin: 0, schedPrin: 0, interest: 0, fedTaxes: 0, stateTax: 0 });

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h6" fontWeight="bold" sx={{ backgroundColor: '#e0e0e0', p: 1, borderRadius: '4px 4px 0 0' }}>
                Cashflow
            </Typography>
            <TableContainer component={Paper} variant="outlined" sx={{ borderRadius: '0 0 4px 4px', borderTop: 'none' }}>
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
                        {data.map((row, index) => (
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
                        <TableRow sx={{ backgroundColor: '#f0f0f0', fontWeight: 'bold' }}>
                            <TableCell align="center">Total</TableCell>
                            <TableCell align="center"></TableCell>
                            <TableCell align="center">{totals.periods.toFixed(2)}</TableCell>
                            <TableCell align="center">{totals.years.toFixed(2)}</TableCell>
                            <TableCell align="center">{totals.totalCash.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                            <TableCell align="center">{totals.totalPrin.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                            <TableCell align="center">{totals.schedPrin.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                            <TableCell align="center">{totals.interest.toLocaleString(undefined, { minimumFractionDigits: 2 })}</TableCell>
                            <TableCell align="center">{totals.fedTaxes.toFixed(2)}</TableCell>
                            <TableCell align="center">{totals.stateTax.toFixed(2)}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default ResultCashflow;
