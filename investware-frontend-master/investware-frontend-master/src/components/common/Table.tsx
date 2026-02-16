import React from 'react';
import {
  Table as MuiTable,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  TablePagination,
  Box,
  Typography,
} from '@mui/material';

export interface Column<T> {
  id: keyof T | string;
  label: string;
  minWidth?: number;
  align?: 'left' | 'right' | 'center';
  format?: (value: any, row: T) => React.ReactNode;
}

interface TableProps<T> {
  columns: Column<T>[];
  data: T[];
  page?: number;
  rowsPerPage?: number;
  totalCount?: number;
  onPageChange?: (page: number) => void;
  onRowsPerPageChange?: (rowsPerPage: number) => void;
  emptyMessage?: string;
  rowKey?: keyof T | ((row: T) => string | number);
}

export function Table<T extends Record<string, any>>({
  columns,
  data,
  page = 0,
  rowsPerPage = 10,
  totalCount,
  onPageChange,
  onRowsPerPageChange,
  emptyMessage = 'No data available',
  rowKey,
}: TableProps<T>) {
  const handleChangePage = (_event: unknown, newPage: number) => {
    onPageChange?.(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    onRowsPerPageChange?.(parseInt(event.target.value, 10));
    onPageChange?.(0);
  };

  const getRowKey = (row: T, index: number): string | number => {
    if (typeof rowKey === 'function') {
      return rowKey(row);
    }
    if (rowKey) {
      return row[rowKey];
    }
    return index;
  };

  return (
    <Paper sx={{ width: '100%', overflow: 'hidden' }}>
      <TableContainer>
        <MuiTable stickyHeader>
          <TableHead>
            <TableRow>
              {columns.map((column) => (
                <TableCell
                  key={column.id as string}
                  align={column.align}
                  style={{ minWidth: column.minWidth }}
                  sx={{
                    fontWeight: 600,
                    backgroundColor: (theme) =>
                      theme.palette.mode === 'light'
                        ? theme.palette.grey[50]
                        : theme.palette.grey[900],
                  }}
                >
                  {column.label}
                </TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {data.length === 0 ? (
              <TableRow>
                <TableCell colSpan={columns.length} align="center">
                  <Box py={4}>
                    <Typography variant="body2" color="text.secondary">
                      {emptyMessage}
                    </Typography>
                  </Box>
                </TableCell>
              </TableRow>
            ) : (
              data.map((row, index) => (
                <TableRow hover key={getRowKey(row, index)} tabIndex={-1}>
                  {columns.map((column) => {
                    const value = row[column.id as keyof T];
                    return (
                      <TableCell key={column.id as string} align={column.align}>
                        {column.format ? column.format(value, row) : value}
                      </TableCell>
                    );
                  })}
                </TableRow>
              ))
            )}
          </TableBody>
        </MuiTable>
      </TableContainer>
      {(onPageChange || onRowsPerPageChange) && (
        <TablePagination
          rowsPerPageOptions={[5, 10, 25, 50]}
          component="div"
          count={totalCount ?? data.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      )}
    </Paper>
  );
}
