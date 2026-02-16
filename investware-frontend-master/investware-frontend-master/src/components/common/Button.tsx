import React from 'react';
import { Button as MuiButton, ButtonProps as MuiButtonProps, CircularProgress } from '@mui/material';

interface ButtonProps extends MuiButtonProps {
  loading?: boolean;
}

export const Button: React.FC<ButtonProps> = ({ 
  children, 
  loading = false, 
  disabled,
  ...props 
}) => {
  return (
    <MuiButton
      {...props}
      disabled={disabled || loading}
      startIcon={loading ? <CircularProgress size={16} color="inherit" /> : props.startIcon}
    >
      {children}
    </MuiButton>
  );
};
