import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import Layout from '@/components/ui/Layout/Layout';
import portfolioService, { Portfolio, Holding } from '@/services/portfolio.service';
import {
  Box,
  Container,
  Typography,
  Card,
  CardContent,
  Button,
  CircularProgress,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
} from '@mui/material';
import { ArrowBack, Add, Delete } from '@mui/icons-material';

const PortfolioDetail = () => {
  const router = useRouter();
  const { id } = router.query;
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null);
  const [holdings, setHoldings] = useState<Holding[]>([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [newHolding, setNewHolding] = useState({
    isin: '',
    amount: '',
    price: '',
    holdDate: new Date().toISOString().split('T')[0],
  });

  useEffect(() => {
    if (id) {
      loadPortfolio();
    }
  }, [id]);

  const loadPortfolio = async () => {
    try {
      setLoading(true);
      const portfolioData = await portfolioService.getPortfolioById(id as string);
      setPortfolio(portfolioData);
      
      const holdingsData = await portfolioService.getHoldings(id as string);
      setHoldings(holdingsData);
    } catch (error) {
      console.error('Error loading portfolio:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddHolding = async () => {
    try {
      await portfolioService.addHolding(id as string, {
        isin: newHolding.isin,
        amount: parseFloat(newHolding.amount),
        price: parseFloat(newHolding.price),
        holdDate: newHolding.holdDate,
      });
      setOpenDialog(false);
      setNewHolding({
        isin: '',
        amount: '',
        price: '',
        holdDate: new Date().toISOString().split('T')[0],
      });
      loadPortfolio();
    } catch (error) {
      console.error('Error adding holding:', error);
    }
  };

  const handleDeleteHolding = async (holdingId: string) => {
    if (confirm('Are you sure you want to delete this holding?')) {
      try {
        await portfolioService.deleteHolding(id as string, holdingId);
        loadPortfolio();
      } catch (error) {
        console.error('Error deleting holding:', error);
      }
    }
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

  if (!portfolio) {
    return (
      <Layout>
        <Container>
          <Typography>Portfolio not found</Typography>
        </Container>
      </Layout>
    );
  }

  const totalValue = holdings.reduce((sum, h) => sum + (h.amount * h.price), 0);

  return (
    <Layout>
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Button
          startIcon={<ArrowBack />}
          onClick={() => router.push('/profile')}
          sx={{ mb: 2 }}
        >
          Back to Profile
        </Button>

        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h4" gutterBottom>
              {portfolio.fullName}
            </Typography>
            <Typography variant="body1" color="text.secondary" gutterBottom>
              {portfolio.abbrevName}
            </Typography>
            {portfolio.strategy && (
              <Typography variant="body2" color="primary">
                Strategy: {portfolio.strategy}
              </Typography>
            )}
            <Typography variant="h6" mt={2}>
              Total Value: ${totalValue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </Typography>
          </CardContent>
        </Card>

        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h5">Holdings</Typography>
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => setOpenDialog(true)}
          >
            Add Holding
          </Button>
        </Box>

        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ISIN</TableCell>
                <TableCell align="right">Amount</TableCell>
                <TableCell align="right">Price</TableCell>
                <TableCell align="right">Total Value</TableCell>
                <TableCell>Hold Date</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {holdings.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    No holdings yet. Add your first holding!
                  </TableCell>
                </TableRow>
              ) : (
                holdings.map((holding) => (
                  <TableRow key={holding.id}>
                    <TableCell>{holding.isin}</TableCell>
                    <TableCell align="right">{holding.amount}</TableCell>
                    <TableCell align="right">${holding.price.toFixed(2)}</TableCell>
                    <TableCell align="right">
                      ${(holding.amount * holding.price).toFixed(2)}
                    </TableCell>
                    <TableCell>
                      {new Date(holding.holdDate).toLocaleDateString()}
                    </TableCell>
                    <TableCell align="center">
                      <Button
                        size="small"
                        color="error"
                        startIcon={<Delete />}
                        onClick={() => handleDeleteHolding(holding.id)}
                      >
                        Delete
                      </Button>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>

        {/* Add Holding Dialog */}
        <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="sm" fullWidth>
          <DialogTitle>Add New Holding</DialogTitle>
          <DialogContent>
            <TextField
              fullWidth
              label="ISIN"
              value={newHolding.isin}
              onChange={(e) => setNewHolding({ ...newHolding, isin: e.target.value })}
              margin="normal"
            />
            <TextField
              fullWidth
              label="Amount"
              type="number"
              value={newHolding.amount}
              onChange={(e) => setNewHolding({ ...newHolding, amount: e.target.value })}
              margin="normal"
            />
            <TextField
              fullWidth
              label="Price"
              type="number"
              value={newHolding.price}
              onChange={(e) => setNewHolding({ ...newHolding, price: e.target.value })}
              margin="normal"
            />
            <TextField
              fullWidth
              label="Hold Date"
              type="date"
              value={newHolding.holdDate}
              onChange={(e) => setNewHolding({ ...newHolding, holdDate: e.target.value })}
              margin="normal"
              InputLabelProps={{ shrink: true }}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
            <Button onClick={handleAddHolding} variant="contained">
              Add
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    </Layout>
  );
};

export default PortfolioDetail;
