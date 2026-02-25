import React, { useState, useEffect } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Button,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Container,
  Avatar,
  Menu,
  MenuItem,
  Divider,
  useMediaQuery,
  useTheme,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import { ThemeToggle } from './ThemeToggle';
import { useRouter } from 'next/router';

interface NavItem {
  label: string;
  href: string;
  icon?: React.ReactNode;
}

const navItems: NavItem[] = [
  { label: 'Dashboard', href: '/' },
  { label: 'Trade', href: '/trade' },
  { label: 'Portfolios', href: '/portfolios/create' },
  { label: 'Profile', href: '/profile' },
  { label: 'Subscription', href: '/subscription' },
];

export const Navbar: React.FC = () => {
  const router = useRouter();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  // ── NEW: Use state + effect pattern ──
  const [isLoggedIn, setIsLoggedIn] = useState(false); // safe default (server + initial client render)

  useEffect(() => {
    // Only runs in browser, after hydration
    const token = localStorage.getItem('token');
    setIsLoggedIn(!!token);
  }, []); // empty deps = run once after mount

  const [mobileOpen, setMobileOpen] = useState(false);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleNavigation = (href: string) => {
    router.push(href);
    setMobileOpen(false);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsLoggedIn(false);           // ← optional: instant UI update
    router.push('/login');
    handleMenuClose();
  };

  const drawer = (
    <Box sx={{ width: 250 }} role="presentation">
      <Box
        sx={{
          p: 2,
          display: 'flex',
          alignItems: 'center',
          gap: 1,
          borderBottom: `1px solid ${theme.palette.divider}`,
        }}
      >
        <ShowChartIcon color="primary" />
        <Typography variant="h6" fontWeight={600}>
          Investware
        </Typography>
      </Box>
      <List>
        {navItems.map((item) => {
          if (item.label === 'Profile' && !isLoggedIn) return null;
          return (
            <ListItem key={item.label} disablePadding>
              <ListItemButton
                onClick={() => handleNavigation(item.href)}
                selected={router.pathname === item.href}
                sx={{
                  '&.Mui-selected': {
                    backgroundColor: theme.palette.action.selected,
                    '&:hover': {
                      backgroundColor: theme.palette.action.hover,
                    },
                  },
                }}
              >
                <ListItemText primary={item.label} />
              </ListItemButton>
            </ListItem>
          );
        })}
      </List>
    </Box>
  );

  return (
    <>
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
            {/* Mobile menu button */}
            {isMobile && (
              <IconButton
                color="inherit"
                aria-label="open drawer"
                edge="start"
                onClick={handleDrawerToggle}
                sx={{ mr: 2 }}
              >
                <MenuIcon />
              </IconButton>
            )}

            {/* Logo */}
            <Box
              sx={{
                display: 'flex',
                alignItems: 'center',
                gap: 1,
                cursor: 'pointer',
              }}
              onClick={() => router.push('/')}
            >
              <ShowChartIcon color="primary" sx={{ fontSize: 28 }} />
              <Typography
                variant="h6"
                component="div"
                sx={{
                  fontWeight: 600,
                  letterSpacing: '-0.5px',
                  display: { xs: 'none', sm: 'block' },
                }}
              >
                Investware
              </Typography>
            </Box>

            {/* Desktop navigation */}
            {!isMobile && (
              <Box sx={{ display: 'flex', gap: 1, ml: 4 }}>
                {navItems.map((item) => {
                  if (item.label === 'Profile' && !isLoggedIn) return null;
                  return (
                    <Button
                      key={item.label}
                      onClick={() => handleNavigation(item.href)}
                      sx={{
                        color: 'text.primary',
                        fontWeight: router.pathname === item.href ? 600 : 400,
                        position: 'relative',
                        '&::after': {
                          content: '""',
                          position: 'absolute',
                          bottom: 0,
                          left: '50%',
                          transform: 'translateX(-50%)',
                          width: router.pathname === item.href ? '80%' : '0%',
                          height: '2px',
                          backgroundColor: 'primary.main',
                          transition: 'width 0.3s ease',
                        },
                        '&:hover::after': {
                          width: '80%',
                        },
                      }}
                    >
                      {item.label}
                    </Button>
                  );
                })}
              </Box>
            )}

            <Box sx={{ flexGrow: 1 }} />

            {/* Right side actions */}
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
              <ThemeToggle />

              {isLoggedIn ? (
                <IconButton onClick={handleMenuOpen} sx={{ ml: 1 }}>
                  <Avatar
                    sx={{
                      width: 32,
                      height: 32,
                      bgcolor: 'primary.main',
                      fontSize: '0.875rem',
                    }}
                  >
                    U
                  </Avatar>
                </IconButton>
              ) : (
                <Button
                  variant="outlined"
                  color="primary"
                  size="small"
                  onClick={() => router.push('/login')}
                >
                  Log In
                </Button>
              )}
            </Box>
          </Toolbar>
        </Container>
      </AppBar>

      {/* Mobile drawer */}
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={handleDrawerToggle}
        ModalProps={{
          keepMounted: true,
        }}
        sx={{
          display: { xs: 'block', md: 'none' },
          '& .MuiDrawer-paper': {
            boxSizing: 'border-box',
            width: 250,
          },
        }}
      >
        {drawer}
      </Drawer>

      {/* User menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
        onClick={handleMenuClose}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
        PaperProps={{
          sx: {
            mt: 1.5,
            minWidth: 200,
          },
        }}
      >
        <MenuItem onClick={() => router.push('/profile')}>
          <AccountCircleIcon sx={{ mr: 1.5, fontSize: 20 }} />
          Profile
        </MenuItem>
        <Divider />
        <MenuItem onClick={handleLogout}>Logout</MenuItem>
      </Menu>
    </>
  );
};