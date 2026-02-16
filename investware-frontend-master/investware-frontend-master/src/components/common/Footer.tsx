import React from 'react';
import { Box, Container, Typography, Link } from '@mui/material';

export const Footer: React.FC = () => {
  return (
    <Box
      component="footer"
      sx={{
        py: 3,
        px: 2,
        mt: 'auto',
        borderTop: (theme) => `1px solid ${theme.palette.divider}`,
        backgroundColor: (theme) =>
          theme.palette.mode === 'light'
            ? theme.palette.background.paper
            : theme.palette.background.default,
      }}
    >
      <Container maxWidth="xl">
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            flexWrap: 'wrap',
            gap: 2,
          }}
        >
          <Typography variant="body2" color="text.secondary">
            © {new Date().getFullYear()} Investware. All rights reserved.
          </Typography>
          <Box sx={{ display: 'flex', gap: 3 }}>
            <Link
              href="#"
              color="text.secondary"
              underline="hover"
              sx={{
                fontSize: '0.875rem',
                transition: 'color 0.2s',
                '&:hover': {
                  color: 'primary.main',
                },
              }}
            >
              Privacy Policy
            </Link>
            <Link
              href="#"
              color="text.secondary"
              underline="hover"
              sx={{
                fontSize: '0.875rem',
                transition: 'color 0.2s',
                '&:hover': {
                  color: 'primary.main',
                },
              }}
            >
              Terms of Service
            </Link>
            <Link
              href="#"
              color="text.secondary"
              underline="hover"
              sx={{
                fontSize: '0.875rem',
                transition: 'color 0.2s',
                '&:hover': {
                  color: 'primary.main',
                },
              }}
            >
              Contact
            </Link>
          </Box>
        </Box>
      </Container>
    </Box>
  );
};
