import React, { useState } from 'react';
import { Box, Typography, Stack, Chip, IconButton, Menu, MenuItem } from '@mui/material';
import {
  Button,
  Card,
  Table,
  PageContainer,
  Section,
  Breadcrumbs,
  Alert,
} from '@/components/common';
import { Grid } from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';
import AddIcon from '@mui/icons-material/Add';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import PieChartIcon from '@mui/icons-material/PieChart';

export default function DashboardExample() {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  // Sample data
  const stats = [
    {
      title: 'Total Portfolio Value',
      value: '$124,532.00',
      change: '+12.5%',
      trend: 'up',
      icon: <AccountBalanceWalletIcon />,
    },
    {
      title: 'Total Return',
      value: '$14,532.00',
      change: '+8.2%',
      trend: 'up',
      icon: <ShowChartIcon />,
    },
    {
      title: 'Active Positions',
      value: '24',
      change: '+3',
      trend: 'up',
      icon: <PieChartIcon />,
    },
  ];

  const recentTransactions = [
    {
      id: 1,
      security: 'US Treasury Bond',
      type: 'Buy',
      amount: '$10,000',
      date: '2024-02-15',
      status: 'Completed',
    },
    {
      id: 2,
      security: 'Corporate Bond AAA',
      type: 'Sell',
      amount: '$5,000',
      date: '2024-02-14',
      status: 'Completed',
    },
    {
      id: 3,
      security: 'Municipal Bond',
      type: 'Buy',
      amount: '$15,000',
      date: '2024-02-13',
      status: 'Pending',
    },
  ];

  const tableColumns = [
    { id: 'security', label: 'Security', minWidth: 200 },
    { id: 'type', label: 'Type', minWidth: 100 },
    { id: 'amount', label: 'Amount', minWidth: 120 },
    { id: 'date', label: 'Date', minWidth: 120 },
    {
      id: 'status',
      label: 'Status',
      minWidth: 120,
      format: (value: string) => (
        <Chip
          label={value}
          size="small"
          color={value === 'Completed' ? 'success' : 'warning'}
        />
      ),
    },
  ];

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  return (
    <PageContainer>
      {/* Breadcrumbs */}
      <Breadcrumbs
        items={[
          { label: 'Home', href: '/' },
          { label: 'Dashboard' },
        ]}
      />

      {/* Page Header */}
      <Box sx={{ mb: 4 }}>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            mb: 2,
          }}
        >
          <Box>
            <Typography variant="h3" gutterBottom fontWeight={600}>
              Dashboard
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Welcome back! Here's an overview of your portfolio.
            </Typography>
          </Box>
          <Button variant="contained" startIcon={<AddIcon />}>
            New Transaction
          </Button>
        </Box>
      </Box>

      {/* Alert */}
      <Alert severity="info" sx={{ mb: 4 }}>
        Market update: Treasury yields increased by 0.15% today. Review your portfolio
        allocations.
      </Alert>

      {/* Stats Cards */}
      <Section title="Portfolio Overview">
        <Grid container spacing={3}>
          {stats.map((stat, index) => (
            <Grid item xs={12} md={4} key={index}>
              <Card>
                <Box
                  sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'flex-start',
                  }}
                >
                  <Box sx={{ flex: 1 }}>
                    <Typography variant="body2" color="text.secondary" gutterBottom>
                      {stat.title}
                    </Typography>
                    <Typography variant="h4" fontWeight={600} gutterBottom>
                      {stat.value}
                    </Typography>
                    <Stack direction="row" spacing={1} alignItems="center">
                      {stat.trend === 'up' ? (
                        <TrendingUpIcon color="success" fontSize="small" />
                      ) : (
                        <TrendingDownIcon color="error" fontSize="small" />
                      )}
                      <Typography
                        variant="body2"
                        color={stat.trend === 'up' ? 'success.main' : 'error.main'}
                        fontWeight={500}
                      >
                        {stat.change}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        vs last month
                      </Typography>
                    </Stack>
                  </Box>
                  <Box
                    sx={{
                      p: 1.5,
                      borderRadius: 2,
                      backgroundColor: (theme) =>
                        theme.palette.mode === 'light'
                          ? theme.palette.grey[100]
                          : theme.palette.grey[800],
                      color: 'primary.main',
                    }}
                  >
                    {stat.icon}
                  </Box>
                </Box>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Section>

      {/* Recent Transactions */}
      <Section
        title="Recent Transactions"
        subtitle="Your latest portfolio activities"
        action={
          <>
            <IconButton onClick={handleMenuOpen}>
              <MoreVertIcon />
            </IconButton>
            <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleMenuClose}>
              <MenuItem onClick={handleMenuClose}>Export</MenuItem>
              <MenuItem onClick={handleMenuClose}>Filter</MenuItem>
              <MenuItem onClick={handleMenuClose}>Refresh</MenuItem>
            </Menu>
          </>
        }
      >
        <Table columns={tableColumns} data={recentTransactions} rowKey="id" />
      </Section>

      {/* Quick Actions */}
      <Section title="Quick Actions">
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <Box sx={{ textAlign: 'center', py: 2 }}>
                <Typography variant="h6" gutterBottom fontWeight={600}>
                  Trade Bonds
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Buy or sell securities
                </Typography>
                <Button variant="outlined" fullWidth>
                  Start Trading
                </Button>
              </Box>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <Box sx={{ textAlign: 'center', py: 2 }}>
                <Typography variant="h6" gutterBottom fontWeight={600}>
                  View Reports
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Analyze performance
                </Typography>
                <Button variant="outlined" fullWidth>
                  View Reports
                </Button>
              </Box>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <Box sx={{ textAlign: 'center', py: 2 }}>
                <Typography variant="h6" gutterBottom fontWeight={600}>
                  Portfolios
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Manage portfolios
                </Typography>
                <Button variant="outlined" fullWidth>
                  Manage
                </Button>
              </Box>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <Box sx={{ textAlign: 'center', py: 2 }}>
                <Typography variant="h6" gutterBottom fontWeight={600}>
                  Settings
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Configure preferences
                </Typography>
                <Button variant="outlined" fullWidth>
                  Configure
                </Button>
              </Box>
            </Card>
          </Grid>
        </Grid>
      </Section>
    </PageContainer>
  );
}
