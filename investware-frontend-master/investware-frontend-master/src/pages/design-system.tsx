import React, { useState } from 'react';
import { Container, Box, Typography, Grid, Divider, Stack, Chip } from '@mui/material';
import {
  Button,
  Input,
  Card,
  Modal,
  Table,
  Alert,
  Tabs,
  EmptyState,
  LoadingSpinner,
  Breadcrumbs,
} from '@/components/common';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';

export default function DesignSystemPage() {
  const [modalOpen, setModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  // Sample table data
  const tableData = [
    { id: 1, name: 'John Doe', email: 'john@example.com', role: 'Admin', status: 'Active' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'User', status: 'Active' },
    { id: 3, name: 'Bob Johnson', email: 'bob@example.com', role: 'User', status: 'Inactive' },
  ];

  const tableColumns = [
    { id: 'name', label: 'Name', minWidth: 150 },
    { id: 'email', label: 'Email', minWidth: 200 },
    { id: 'role', label: 'Role', minWidth: 100 },
    {
      id: 'status',
      label: 'Status',
      minWidth: 100,
      format: (value: string) => (
        <Chip
          label={value}
          size="small"
          color={value === 'Active' ? 'success' : 'default'}
        />
      ),
    },
  ];

  const handleLoadingDemo = () => {
    setLoading(true);
    setTimeout(() => setLoading(false), 2000);
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Breadcrumbs */}
      <Breadcrumbs
        items={[
          { label: 'Home', href: '/' },
          { label: 'Design System' },
        ]}
      />

      {/* Header */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" gutterBottom fontWeight={600}>
          Design System
        </Typography>
        <Typography variant="body1" color="text.secondary">
          A comprehensive collection of reusable components with light and dark mode support.
        </Typography>
      </Box>

      {/* Typography Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Typography
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Stack spacing={2}>
          <Typography variant="h1">Heading 1</Typography>
          <Typography variant="h2">Heading 2</Typography>
          <Typography variant="h3">Heading 3</Typography>
          <Typography variant="h4">Heading 4</Typography>
          <Typography variant="h5">Heading 5</Typography>
          <Typography variant="h6">Heading 6</Typography>
          <Typography variant="body1">
            Body 1: This is a paragraph with regular body text. It's designed for readability
            and clarity.
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Body 2: Secondary text with a smaller font size, perfect for supporting information.
          </Typography>
        </Stack>
      </Box>

      {/* Buttons Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Buttons
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Grid container spacing={2}>
          <Grid item>
            <Button variant="contained">Primary Button</Button>
          </Grid>
          <Grid item>
            <Button variant="outlined">Outlined Button</Button>
          </Grid>
          <Grid item>
            <Button variant="text">Text Button</Button>
          </Grid>
          <Grid item>
            <Button variant="contained" startIcon={<AddIcon />}>
              With Icon
            </Button>
          </Grid>
          <Grid item>
            <Button variant="contained" disabled>
              Disabled
            </Button>
          </Grid>
          <Grid item>
            <Button variant="contained" loading={loading} onClick={handleLoadingDemo}>
              Loading State
            </Button>
          </Grid>
        </Grid>
        <Box sx={{ mt: 2 }}>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            Button Sizes:
          </Typography>
          <Stack direction="row" spacing={2}>
            <Button variant="contained" size="small">
              Small
            </Button>
            <Button variant="contained" size="medium">
              Medium
            </Button>
            <Button variant="contained" size="large">
              Large
            </Button>
          </Stack>
        </Box>
      </Box>

      {/* Inputs Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Input Fields
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Input label="Standard Input" placeholder="Enter text..." />
          </Grid>
          <Grid item xs={12} md={6}>
            <Input label="With Helper Text" helperText="This is helper text" />
          </Grid>
          <Grid item xs={12} md={6}>
            <Input label="Required Field" required />
          </Grid>
          <Grid item xs={12} md={6}>
            <Input label="Disabled" disabled value="Disabled input" />
          </Grid>
          <Grid item xs={12} md={6}>
            <Input label="With Icon" InputProps={{ startAdornment: <SearchIcon /> }} />
          </Grid>
          <Grid item xs={12} md={6}>
            <Input label="Error State" error helperText="This field has an error" />
          </Grid>
        </Grid>
      </Box>

      {/* Cards Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Cards
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <Card title="Simple Card" subtitle="With subtitle">
              <Typography variant="body2">
                This is a simple card with a title, subtitle, and content area.
              </Typography>
            </Card>
          </Grid>
          <Grid item xs={12} md={4}>
            <Card
              title="Card with Actions"
              actions={
                <Stack direction="row" spacing={1}>
                  <Button size="small" startIcon={<EditIcon />}>
                    Edit
                  </Button>
                  <Button size="small" color="error" startIcon={<DeleteIcon />}>
                    Delete
                  </Button>
                </Stack>
              }
            >
              <Typography variant="body2">
                This card includes action buttons at the bottom.
              </Typography>
            </Card>
          </Grid>
          <Grid item xs={12} md={4}>
            <Card
              title="With Header Action"
              headerAction={
                <Button size="small" variant="outlined">
                  Action
                </Button>
              }
            >
              <Typography variant="body2">
                This card has an action button in the header.
              </Typography>
            </Card>
          </Grid>
        </Grid>
      </Box>

      {/* Alerts Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Alerts
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Stack spacing={2}>
          <Alert severity="success" title="Success">
            This is a success alert with a title.
          </Alert>
          <Alert severity="info">This is an info alert without a title.</Alert>
          <Alert severity="warning" title="Warning">
            This is a warning alert.
          </Alert>
          <Alert severity="error" title="Error">
            This is an error alert.
          </Alert>
        </Stack>
      </Box>

      {/* Table Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Table
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Table columns={tableColumns} data={tableData} rowKey="id" />
      </Box>

      {/* Tabs Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Tabs
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Card>
          <Tabs
            tabs={[
              {
                label: 'Overview',
                content: (
                  <Typography variant="body2">
                    This is the overview tab content. Tabs are great for organizing related
                    content.
                  </Typography>
                ),
              },
              {
                label: 'Details',
                content: (
                  <Typography variant="body2">
                    This is the details tab content with more specific information.
                  </Typography>
                ),
              },
              {
                label: 'Settings',
                content: (
                  <Typography variant="body2">
                    This is the settings tab where configuration options would go.
                  </Typography>
                ),
              },
            ]}
          />
        </Card>
      </Box>

      {/* Empty State Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Empty State
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Card>
          <EmptyState
            title="No items found"
            description="Get started by creating your first item."
            action={{
              label: 'Create Item',
              onClick: () => alert('Create item clicked'),
            }}
          />
        </Card>
      </Box>

      {/* Loading Spinner Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Loading Spinner
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Card>
          <LoadingSpinner message="Loading data..." />
        </Card>
      </Box>

      {/* Modal Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h4" gutterBottom fontWeight={600}>
          Modal
        </Typography>
        <Divider sx={{ mb: 3 }} />
        <Button variant="contained" onClick={() => setModalOpen(true)}>
          Open Modal
        </Button>
        <Modal
          open={modalOpen}
          onClose={() => setModalOpen(false)}
          title="Example Modal"
          actions={
            <>
              <Button variant="outlined" onClick={() => setModalOpen(false)}>
                Cancel
              </Button>
              <Button variant="contained" onClick={() => setModalOpen(false)}>
                Confirm
              </Button>
            </>
          }
        >
          <Typography variant="body2">
            This is a modal dialog. It can contain any content and has customizable actions.
          </Typography>
        </Modal>
      </Box>
    </Container>
  );
}
