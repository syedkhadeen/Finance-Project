import React, { ReactNode } from 'react';
import { Box, Typography, Divider } from '@mui/material';

interface SectionProps {
  title?: string;
  subtitle?: string;
  children: ReactNode;
  action?: ReactNode;
  divider?: boolean;
}

export const Section: React.FC<SectionProps> = ({
  title,
  subtitle,
  children,
  action,
  divider = true,
}) => {
  return (
    <Box sx={{ mb: 4 }}>
      {(title || subtitle || action) && (
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'flex-start',
            mb: divider ? 2 : 3,
          }}
        >
          <Box>
            {title && (
              <Typography variant="h5" gutterBottom fontWeight={600}>
                {title}
              </Typography>
            )}
            {subtitle && (
              <Typography variant="body2" color="text.secondary">
                {subtitle}
              </Typography>
            )}
          </Box>
          {action && <Box>{action}</Box>}
        </Box>
      )}
      {divider && title && <Divider sx={{ mb: 3 }} />}
      {children}
    </Box>
  );
};
