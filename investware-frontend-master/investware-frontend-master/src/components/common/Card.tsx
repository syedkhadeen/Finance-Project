import React, { ReactNode } from 'react';
import { Card as MuiCard, CardContent, CardHeader, CardActions, CardProps as MuiCardProps } from '@mui/material';

interface CardProps extends MuiCardProps {
  title?: string;
  subtitle?: string;
  actions?: ReactNode;
  children: ReactNode;
  headerAction?: ReactNode;
}

export const Card: React.FC<CardProps> = ({
  title,
  subtitle,
  actions,
  children,
  headerAction,
  ...props
}) => {
  return (
    <MuiCard {...props}>
      {(title || subtitle) && (
        <CardHeader
          title={title}
          subheader={subtitle}
          action={headerAction}
          sx={{
            '& .MuiCardHeader-title': {
              fontSize: '1.125rem',
              fontWeight: 600,
            },
            '& .MuiCardHeader-subheader': {
              fontSize: '0.875rem',
            },
          }}
        />
      )}
      <CardContent>{children}</CardContent>
      {actions && <CardActions sx={{ px: 2, pb: 2 }}>{actions}</CardActions>}
    </MuiCard>
  );
};
