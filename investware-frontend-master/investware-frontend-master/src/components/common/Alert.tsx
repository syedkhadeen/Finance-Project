import React from 'react';
import { Alert as MuiAlert, AlertTitle, AlertProps as MuiAlertProps } from '@mui/material';

interface AlertProps extends MuiAlertProps {
  title?: string;
}

export const Alert: React.FC<AlertProps> = ({ title, children, ...props }) => {
  return (
    <MuiAlert {...props}>
      {title && <AlertTitle>{title}</AlertTitle>}
      {children}
    </MuiAlert>
  );
};
