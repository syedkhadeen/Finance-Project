import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import authService from '@/services/auth.service';
import portfolioService, { Portfolio } from '@/services/portfolio.service';
import {
  Box,
  Container,
  Typography,
  Grid,
  CircularProgress,
  Avatar,
  Chip,
  Paper,
} from '@mui/material';
import { Card, Button, Breadcrumbs, EmptyState, Section } from '@/components/common';
import { AccountCircle, Logout, Add, TrendingUp, FolderOpen } from '@mui/icons-material';

const ProfilePage = () => {
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [portfolios, setPortfolios] = useState<Portfolio[]>([]);
  const [userEmail, setUserEmail] = useState('');

  useEffect(() => {
    if (!authService.isAuthenticated()) {
      router.push('/login');
      return;
    }

    loadData();
  }, [router]);

  const loadData = async () => {
    try {
      setLoading(true);
      const token = authService.getToken();
      if (token) {
        // Decode JWT to get user email
        const payload = JSON.parse(atob(token.split('.')[1]));
        setUserEmail(payload.email || '');
      }

      const portfoliosData = await portfolioService.getPortfolios();
      setPortfolios(portfoliosData);
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    authService.logout();
  };

  const handleCreatePortfolio = () => {
    router.push('/portfolios/create');
  };

  const handleViewPortfolio = (portfolioId: string) => {
    router.push(`/portfolios/${portfolioId}`);
  };

  const handleGoToTrade = () => {
    router.push('/trade');
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      <Breadcrumbs
        items={[
          { label: 'Home', href: '/' },
          { label: 'Profile' },
        ]}
      />

      {/* Header */}
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={4} flexWrap="wrap" gap={2}>
        <Box display="flex" alignItems="center" gap={2}>
          <Avatar
            sx={{
              width: 64,
              height: 64,
              bgcolor: 'primary.main',
              fontSize: '2rem',
            }}
          >
            {userEmail?.[0]?.toUpperCase() || 'U'}
          </Avatar>
          <Box>
            <Typography variant="h4" component="h1" fontWeight={600}>
              My Profile
            </Typography>
            <Typography variant="body1" color="text.secondary">
              {userEmail}
            </Typography>
          </Box>
        </Box>
        <Button
          variant="outlined"
          color="error"
          startIcon={<Logout />}
          onClick={handleLogout}
        >
          Logout
        </Button>
      </Box>

      {/* Quick Actions */}
      <Section title="Quick Actions">
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Paper
              elevation={0}
              sx={{
                p: 3,
                border: (theme) => `1px solid ${theme.palette.divider}`,
                borderRadius: 2,
                cursor: 'pointer',
                transition: 'all 0.3s ease',
                '&:hover': {
                  transform: 'translateY(-2px)',
                  boxShadow: (theme) => theme.palette.mode === 'light'
                    ? '0 8px 16px rgba(0,0,0,0.1)'
                    : '0 8px 16px rgba(0,0,0,0.4)',
                  borderColor: 'primary.main',
                },
              }}
              onClick={handleGoToTrade}
            >
              <Box display="flex" alignItems="center" gap={2}>
                <Box
                  sx={{
                    p: 1.5,
                    borderRadius: 2,
                    bgcolor: (theme) =>
                      theme.palette.mode === 'light'
                        ? 'primary.50'
                        : 'primary.900',
                    color: 'primary.main',
                  }}
                >
                  <TrendingUp sx={{ fontSize: 32 }} />
                </Box>
                <Box>
                  <Typography variant="h6" fontWeight={600}>
                    Bond Calculator
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Calculate bond analytics and perform trade analysis
                  </Typography>
                </Box>
              </Box>
            </Paper>
          </Grid>
          <Grid item xs={12} md={6}>
            <Paper
              elevation={0}
              sx={{
                p: 3,
                border: (theme) => `1px solid ${theme.palette.divider}`,
                borderRadius: 2,
                cursor: 'pointer',
                transition: 'all 0.3s ease',
                '&:hover': {
                  transform: 'translateY(-2px)',
                  boxShadow: (theme) => theme.palette.mode === 'light'
                    ? '0 8px 16px rgba(0,0,0,0.1)'
                    : '0 8px 16px rgba(0,0,0,0.4)',
                  borderColor: 'success.main',
                },
              }}
              onClick={handleCreatePortfolio}
            >
              <Box display="flex" alignItems="center" gap={2}>
                <Box
                  sx={{
                    p: 1.5,
                    borderRadius: 2,
                    bgcolor: (theme) =>
                      theme.palette.mode === 'light'
                        ? 'success.50'
                        : 'success.900',
                    color: 'success.main',
                  }}
                >
                  <Add sx={{ fontSize: 32 }} />
                </Box>
                <Box>
                  <Typography variant="h6" fontWeight={600}>
                    Create Portfolio
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Start a new bond portfolio
                  </Typography>
                </Box>
              </Box>
            </Paper>
          </Grid>
        </Grid>
      </Section>

      {/* Portfolios Section */}
      <Section
        title="My Portfolios"
        subtitle={`You have ${portfolios.length} portfolio${portfolios.length !== 1 ? 's' : ''}`}
        action={
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={handleCreatePortfolio}
          >
            New Portfolio
          </Button>
        }
      >
        {portfolios.length === 0 ? (
          <EmptyState
            icon={<FolderOpen sx={{ fontSize: 64 }} />}
            title="No portfolios yet"
            description="Create your first portfolio to start managing your bond investments"
            action={{
              label: 'Create Portfolio',
              onClick: handleCreatePortfolio,
            }}
          />
        ) : (
          <Grid container spacing={3}>
            {portfolios.map((portfolio) => (
              <Grid item xs={12} md={6} lg={4} key={portfolio.id}>
                <Card
                  sx={{
                    cursor: 'pointer',
                    height: '100%',
                    transition: 'all 0.3s ease',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                    },
                  }}
                  onClick={() => handleViewPortfolio(portfolio.id)}
                >
                  <Box>
                    <Typography variant="h6" gutterBottom fontWeight={600}>
                      {portfolio.fullName}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" gutterBottom>
                      {portfolio.abbrevName}
                    </Typography>
                    {portfolio.strategy && (
                      <Chip
                        label={portfolio.strategy}
                        size="small"
                        color="primary"
                        sx={{ mt: 1 }}
                      />
                    )}
                    <Typography variant="caption" color="text.secondary" display="block" mt={2}>
                      Created: {new Date(portfolio.createdAt).toLocaleDateString()}
                    </Typography>
                  </Box>
                </Card>
              </Grid>
            ))}
          </Grid>
        )}
      </Section>
    </Container>
  );
};

export default ProfilePage;
