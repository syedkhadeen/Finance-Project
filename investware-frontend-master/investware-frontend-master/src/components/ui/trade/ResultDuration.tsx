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

interface DurationItem {
    tenor: string;
    duration: number;
}

interface ResultDurationProps {
    data: DurationItem[];
}

const ResultDuration: React.FC<ResultDurationProps> = ({ data }) => {
    if (!data || data.length === 0) return null;

    return (
        <Box sx={{ mt: 4 }}>
            <Typography variant="h6" fontWeight="bold" sx={{ backgroundColor: '#e0e0e0', p: 1, borderRadius: '4px 4px 0 0' }}>
                Key Rate Duration
            </Typography>
            <TableContainer component={Paper} variant="outlined" sx={{ borderRadius: '0 0 4px 4px', borderTop: 'none' }}>
                <Table size="small">
                    <TableHead sx={{ backgroundColor: '#f5f5f5' }}>
                        <TableRow>
                            <TableCell align="center" sx={{ fontWeight: 'bold' }}>Key Rate Term</TableCell>
                            <TableCell align="center" sx={{ fontWeight: 'bold' }}>Key Rate(%)</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((item, index) => (
                            <TableRow key={index}>
                                <TableCell align="center">{item.tenor}</TableCell>
                                <TableCell align="center">{item.duration.toFixed(2)}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default ResultDuration;
