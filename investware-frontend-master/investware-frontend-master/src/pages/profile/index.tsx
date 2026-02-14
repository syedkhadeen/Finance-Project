import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import Layout from '@/components/ui/Layout/Layout';
import authService from '@/services/auth.service';
import portfolioService, { Portfolio } from '@/services/portfolio.service';
import {
  Box,
  Container,
  Typography,
  Card,
  CardContent,
  Grid,
  Button,
  CircularProgress,
} from '@mui/material';
import { AccountCircle, Logout, Add, TrendingUp } from '@mui/icons-material';

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
  }, []);

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
      <Layout>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
          <CircularProgress />
        </Box>
      </Layout>
    );
  }

  return (
    <Layout>
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        {/* Header */}
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={4}>
          <Box display="flex" alignItems="center" gap={2}>
            <AccountCircle sx={{ fontSize: 48, color: 'primary.main' }} />
            <Box>
              <Typography variant="h4" component="h1">
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
        <Grid container spacing={3} mb={4}>
          <Grid item xs={12} md={6}>
            <Card sx={{ height: '100%', cursor: 'pointer' }} onClick={handleGoToTrade}>
              <CardContent>
                <Box display="flex" alignItems="center" gap={2}>
                  <TrendingUp sx={{ fontSize: 40, color: 'primary.main' }} />
                  <Box>
                    <Typography variant="h6">Bond Calculator</Typography>
                    <Typography variant="body2" color="text.secondary">
                      Calculate bond analytics and perform trade analysis
                    </Typography>
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} md={6}>
            <Card sx={{ height: '100%', cursor: 'pointer' }} onClick={handleCreatePortfolio}>
              <CardContent>
                <Box display="flex" alignItems="center" gap={2}>
                  <Add sx={{ fontSize: 40, color: 'success.main' }} />
                  <Box>
                    <Typography variant="h6">Create Portfolio</Typography>
                    <Typography variant="body2" color="text.secondary">
                      Start a new bond portfolio
                    </Typography>
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        </Grid>

        {/* Portfolios Section */}
        <Box mb={4}>
          <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
            <Typography variant="h5" component="h2">
              My Portfolios
            </Typography>
            <Button
              variant="contained"
              startIcon={<Add />}
              onClick={handleCreatePortfolio}
            >
              New Portfolio
            </Button>
          </Box>

          {portfolios.length === 0 ? (
            <Card>
              <CardContent>
                <Typography variant="body1" color="text.secondary" textAlign="center" py={4}>
                  You don't have any portfolios yet. Create your first portfolio to get started!
                </Typography>
              </CardContent>
            </Card>
          ) : (
            <Grid container spacing={3}>
              {portfolios.map((portfolio) => (
                <Grid item xs={12} md={6} key={portfolio.id}>
                  <Card
                    sx={{
                      cursor: 'pointer',
                      '&:hover': {
                        boxShadow: 6,
                      },
                    }}
                    onClick={() => handleViewPortfolio(portfolio.id)}
                  >
                    <CardContent>
                      <Typography variant="h6" gutterBottom>
                        {portfolio.fullName}
                      </Typography>
                      <Typography variant="body2" color="text.secondary" gutterBottom>
                        {portfolio.abbrevName}
                      </Typography>
                      {portfolio.strategy && (
                        <Typography variant="body2" color="primary">
                          Strategy: {portfolio.strategy}
                        </Typography>
                      )}
                      <Typography variant="caption" color="text.secondary" display="block" mt={1}>
                        Created: {new Date(portfolio.createdAt).toLocaleDateString()}
                      </Typography>
                    </CardContent>
                  </Card>
                </Grid>
              ))}
            </Grid>
          )}
        </Box>
      </Container>
    </Layout>
  );
};

export default ProfilePage;
