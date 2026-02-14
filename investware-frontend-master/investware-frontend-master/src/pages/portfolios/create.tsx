import { useState, useEffect } from 'react';
import { useRouter } from 'next/router';
import Layout from '@/components/ui/Layout/Layout';
import portfolioService from '@/services/portfolio.service';
import authService from '@/services/auth.service';
import {
  Box,
  Container,
  Typography,
  Card,
  CardContent,
  TextField,
  Button,
  Alert,
  CircularProgress,
} from '@mui/material';
import { ArrowBack, Save } from '@mui/icons-material';

const CreatePortfolio = () => {
  const router = useRouter();
  const [formData, setFormData] = useState({
    abbrevName: '',
    fullName: '',
    strategy: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    // Check if user is authenticated
    if (!authService.isAuthenticated()) {
      router.push('/login');
    } else {
      setChecking(false);
    }
  }, [router]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      console.log('portfolioService:', portfolioService);
      console.log('formData:', formData);
      
      const portfolio = await portfolioService.createPortfolio(formData);
      console.log('Created portfolio:', portfolio);
      router.push(`/portfolios/${portfolio.id}`);
    } catch (err: any) {
      console.error('Portfolio creation error:', err);
      setError(err.response?.data?.message || err.message || 'Failed to create portfolio');
    } finally {
      setLoading(false);
    }
  };

  if (checking) {
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
      <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
        <Button
          startIcon={<ArrowBack />}
          onClick={() => router.back()}
          sx={{ mb: 2 }}
        >
          Back
        </Button>

        <Typography variant="h4" component="h1" gutterBottom>
          Create New Portfolio
        </Typography>

        <Card>
          <CardContent>
            <Box component="form" onSubmit={handleSubmit}>
              {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                  {error}
                </Alert>
              )}

              <TextField
                fullWidth
                label="Abbreviation Name"
                name="abbrevName"
                value={formData.abbrevName}
                onChange={handleChange}
                required
                margin="normal"
                helperText="Short name for the portfolio (e.g., TECH_PORT)"
              />

              <TextField
                fullWidth
                label="Full Name"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                required
                margin="normal"
                helperText="Full descriptive name"
              />

              <TextField
                fullWidth
                label="Strategy"
                name="strategy"
                value={formData.strategy}
                onChange={handleChange}
                margin="normal"
                helperText="Investment strategy (optional)"
              />

              <Box mt={3} display="flex" gap={2}>
                <Button
                  type="submit"
                  variant="contained"
                  startIcon={<Save />}
                  disabled={loading}
                  fullWidth
                >
                  {loading ? 'Creating...' : 'Create Portfolio'}
                </Button>
                <Button
                  variant="outlined"
                  onClick={() => router.back()}
                  disabled={loading}
                >
                  Cancel
                </Button>
              </Box>
            </Box>
          </CardContent>
        </Card>
      </Container>
    </Layout>
  );
};

export default CreatePortfolio;
