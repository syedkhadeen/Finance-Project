import React from 'react';
import { AppBar, Toolbar, Typography, Box, Container } from '@mui/material';
import { ThemeToggle } from './ThemeToggle';

interface HeaderProps {
  title?: string;
  children?: React.ReactNode;
}

export const Header: React.FC<HeaderProps> = ({ title = 'Investware', children }) => {
  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        backgroundColor: (theme) =>
          theme.palette.mode === 'light'
            ? theme.palette.background.paper
            : theme.palette.background.default,
        borderBottom: (theme) => `1px solid ${theme.palette.divider}`,
        color: (theme) => theme.palette.text.primary,
      }}
    >
      <Container maxWidth="xl">
        <Toolbar disableGutters sx={{ minHeight: 64 }}>
          <Typography
            variant="h6"
            component="div"
            sx={{
              fontWeight: 600,
              letterSpacing: '-0.5px',
            }}
          >
            {title}
          </Typography>
          <Box sx={{ flexGrow: 1 }} />
          {children}
          <ThemeToggle />
        </Toolbar>
      </Container>
    </AppBar>
  );
};
