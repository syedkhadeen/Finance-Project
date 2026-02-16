import { Container, Box, Typography, Grid, Paper } from '@mui/material';
import { Button, Card, Section } from '@/components/common';
import { useRouter } from 'next/router';
import { useEffect } from 'react';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import SecurityIcon from '@mui/icons-material/Security';
import authService from '@/services/auth.service';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // Redirect to profile if already logged in
    if (authService.isAuthenticated()) {
      router.push('/profile');
    }
  }, [router]);

  const features = [
    {
      icon: <ShowChartIcon sx={{ fontSize: 48 }} />,
      title: 'Bond Trading',
      description: 'Execute trades with real-time pricing and comprehensive analytics',
      action: () => router.push('/trade'),
    },
    {
      icon: <AccountBalanceWalletIcon sx={{ fontSize: 48 }} />,
      title: 'Portfolio Management',
      description: 'Track and manage your bond portfolios with detailed insights',
      action: () => router.push('/portfolios/create'),
    },
    {
      icon: <TrendingUpIcon sx={{ fontSize: 48 }} />,
      title: 'Advanced Analytics',
      description: 'Yield curves, duration, convexity, and key rate analysis',
      action: () => router.push('/trade'),
    },
    {
      icon: <SecurityIcon sx={{ fontSize: 48 }} />,
      title: 'Secure & Reliable',
      description: 'Enterprise-grade security with professional calculation engine',
      action: () => router.push('/profile'),
    },
  ];

  return (
    <Box
      sx={{
        minHeight: 'calc(100vh - 64px)',
        background: (theme) =>
          theme.palette.mode === 'light'
            ? 'linear-gradient(180deg, rgba(74, 144, 226, 0.05) 0%, rgba(255, 255, 255, 0) 100%)'
            : 'linear-gradient(180deg, rgba(74, 144, 226, 0.1) 0%, rgba(26, 29, 35, 0) 100%)',
      }}
    >
      {/* Hero Section */}
      <Container maxWidth="xl">
        <Box
          sx={{
            pt: { xs: 8, md: 12 },
            pb: { xs: 6, md: 10 },
            textAlign: 'center',
          }}
        >
          <Typography
            variant="h2"
            component="h1"
            gutterBottom
            sx={{
              fontWeight: 700,
              fontSize: { xs: '2rem', sm: '2.5rem', md: '3rem' },
              mb: 2,
            }}
          >
            Professional Bond Trading Platform
          </Typography>
          <Typography
            variant="h6"
            color="text.secondary"
            sx={{
              mb: 4,
              maxWidth: 700,
              mx: 'auto',
              fontSize: { xs: '1rem', md: '1.25rem' },
              fontWeight: 400,
            }}
          >
            Advanced analytics, real-time calculations, and comprehensive portfolio management
            for fixed income securities
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
            <Button
              variant="contained"
              size="large"
              onClick={() => router.push('/trade')}
              sx={{ px: 4, py: 1.5 }}
            >
              Start Trading
            </Button>
            <Button
              variant="outlined"
              size="large"
              onClick={() => router.push('/login')}
              sx={{ px: 4, py: 1.5 }}
            >
              Sign In
            </Button>
          </Box>
        </Box>

        {/* Features Grid */}
        <Section>
          <Grid container spacing={3}>
            {features.map((feature, index) => (
              <Grid item xs={12} sm={6} md={3} key={index}>
                <Paper
                  elevation={0}
                  sx={{
                    p: 3,
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    textAlign: 'center',
                    border: (theme) => `1px solid ${theme.palette.divider}`,
                    borderRadius: 2,
                    transition: 'all 0.3s ease',
                    cursor: 'pointer',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                      boxShadow: (theme) =>
                        theme.palette.mode === 'light'
                          ? '0 12px 24px rgba(0,0,0,0.1)'
                          : '0 12px 24px rgba(0,0,0,0.4)',
                      borderColor: 'primary.main',
                    },
                  }}
                  onClick={feature.action}
                >
                  <Box
                    sx={{
                      color: 'primary.main',
                      mb: 2,
                    }}
                  >
                    {feature.icon}
                  </Box>
                  <Typography variant="h6" gutterBottom fontWeight={600}>
                    {feature.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {feature.description}
                  </Typography>
                </Paper>
              </Grid>
            ))}
          </Grid>
        </Section>

        {/* Stats Section */}
        <Section>
          <Card>
            <Grid container spacing={4}>
              <Grid item xs={12} md={4}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h3" color="primary.main" fontWeight={700}>
                    99.9%
                  </Typography>
                  <Typography variant="body1" color="text.secondary" sx={{ mt: 1 }}>
                    Calculation Accuracy
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} md={4}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h3" color="primary.main" fontWeight={700}>
                    &lt;1s
                  </Typography>
                  <Typography variant="body1" color="text.secondary" sx={{ mt: 1 }}>
                    Average Response Time
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} md={4}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h3" color="primary.main" fontWeight={700}>
                    24/7
                  </Typography>
                  <Typography variant="body1" color="text.secondary" sx={{ mt: 1 }}>
                    Platform Availability
                  </Typography>
                </Box>
              </Grid>
            </Grid>
          </Card>
        </Section>

        {/* CTA Section */}
        <Box
          sx={{
            py: 8,
            textAlign: 'center',
          }}
        >
          <Typography variant="h4" gutterBottom fontWeight={600}>
            Ready to get started?
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
            Join professionals who trust Investware for their bond trading needs
          </Typography>
          <Button
            variant="contained"
            size="large"
            onClick={() => router.push('/sign-up')}
            sx={{ px: 4, py: 1.5 }}
          >
            Create Account
          </Button>
        </Box>
      </Container>
    </Box>
  );
}
