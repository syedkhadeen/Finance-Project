import React, { ReactNode } from 'react';
import { Grid as MuiGrid, GridProps as MuiGridProps } from '@mui/material';

interface GridProps extends MuiGridProps {
  children: ReactNode;
}

export const Grid: React.FC<GridProps> = ({ children, ...props }) => {
  return <MuiGrid {...props}>{children}</MuiGrid>;
};
