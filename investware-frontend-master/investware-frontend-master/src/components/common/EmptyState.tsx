import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import InboxIcon from '@mui/icons-material/Inbox';

interface EmptyStateProps {
  icon?: React.ReactNode;
  title: string;
  description?: string;
  action?: {
    label: string;
    onClick: () => void;
  };
}

export const EmptyState: React.FC<EmptyStateProps> = ({
  icon,
  title,
  description,
  action,
}) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        py: 8,
        px: 2,
        textAlign: 'center',
      }}
    >
      <Box
        sx={{
          color: 'text.disabled',
          mb: 2,
          '& > svg': {
            fontSize: 64,
          },
        }}
      >
        {icon || <InboxIcon sx={{ fontSize: 64 }} />}
      </Box>
      <Typography variant="h6" color="text.primary" gutterBottom fontWeight={600}>
        {title}
      </Typography>
      {description && (
        <Typography variant="body2" color="text.secondary" sx={{ mb: 3, maxWidth: 400 }}>
          {description}
        </Typography>
      )}
      {action && (
        <Button variant="contained" onClick={action.onClick}>
          {action.label}
        </Button>
      )}
    </Box>
  );
};
