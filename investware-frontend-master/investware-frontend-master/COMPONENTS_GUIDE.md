# Component Usage Guide

Quick reference for all available components in the design system.

## 🎯 Import Statement

```tsx
import {
  Button,
  Input,
  Card,
  Modal,
  Table,
  Alert,
  Tabs,
  Header,
  Footer,
  ThemeToggle,
  LoadingSpinner,
  EmptyState,
  Breadcrumbs,
  PageContainer,
  Section,
  Grid,
} from '@/components/common';
```

## 📦 Components Quick Reference

### 1. Button

```tsx
// Basic variants
<Button variant="contained">Primary</Button>
<Button variant="outlined">Outlined</Button>
<Button variant="text">Text</Button>

// With loading state
<Button variant="contained" loading={isLoading}>
  Save
</Button>

// With icon
<Button startIcon={<AddIcon />}>Add Item</Button>

// Sizes
<Button size="small">Small</Button>
<Button size="medium">Medium</Button>
<Button size="large">Large</Button>

// Colors
<Button color="primary">Primary</Button>
<Button color="secondary">Secondary</Button>
<Button color="error">Error</Button>
<Button color="success">Success</Button>
```

### 2. Input

```tsx
// Basic input
<Input label="Email" placeholder="Enter email..." />

// With validation
<Input 
  label="Password" 
  type="password" 
  required 
  error={hasError}
  helperText="Password must be 8+ characters"
/>

// With icon
<Input 
  label="Search" 
  InputProps={{ 
    startAdornment: <SearchIcon /> 
  }} 
/>

// Multiline
<Input 
  label="Description" 
  multiline 
  rows={4} 
/>
```

### 3. Card

```tsx
// Simple card
<Card title="Card Title" subtitle="Subtitle">
  <p>Content goes here</p>
</Card>

// With actions
<Card 
  title="User Profile"
  actions={
    <>
      <Button>Cancel</Button>
      <Button variant="contained">Save</Button>
    </>
  }
>
  <p>Profile content</p>
</Card>

// With header action
<Card 
  title="Settings"
  headerAction={
    <IconButton>
      <MoreVertIcon />
    </IconButton>
  }
>
  <p>Settings content</p>
</Card>
```

### 4. Modal

```tsx
const [open, setOpen] = useState(false);

<Button onClick={() => setOpen(true)}>Open Modal</Button>

<Modal
  open={open}
  onClose={() => setOpen(false)}
  title="Confirm Action"
  maxWidth="sm"
  actions={
    <>
      <Button onClick={() => setOpen(false)}>Cancel</Button>
      <Button variant="contained" onClick={handleConfirm}>
        Confirm
      </Button>
    </>
  }
>
  <p>Are you sure you want to proceed?</p>
</Modal>
```

### 5. Table

```tsx
const columns = [
  { id: 'name', label: 'Name', minWidth: 150 },
  { id: 'email', label: 'Email', minWidth: 200 },
  { 
    id: 'status', 
    label: 'Status',
    format: (value) => (
      <Chip 
        label={value} 
        color={value === 'Active' ? 'success' : 'default'} 
      />
    )
  },
];

const data = [
  { id: 1, name: 'John Doe', email: 'john@example.com', status: 'Active' },
  { id: 2, name: 'Jane Smith', email: 'jane@example.com', status: 'Inactive' },
];

<Table
  columns={columns}
  data={data}
  rowKey="id"
  page={page}
  rowsPerPage={10}
  totalCount={100}
  onPageChange={setPage}
  onRowsPerPageChange={setRowsPerPage}
  emptyMessage="No users found"
/>
```

### 6. Alert

```tsx
// Success
<Alert severity="success" title="Success!">
  Operation completed successfully
</Alert>

// Error
<Alert severity="error" title="Error">
  Something went wrong
</Alert>

// Warning
<Alert severity="warning">
  Please review your input
</Alert>

// Info
<Alert severity="info">
  New features available
</Alert>
```

### 7. Tabs

```tsx
<Tabs
  tabs={[
    {
      label: 'Overview',
      content: <div>Overview content</div>
    },
    {
      label: 'Details',
      content: <div>Details content</div>
    },
    {
      label: 'Settings',
      content: <div>Settings content</div>,
      disabled: true
    },
  ]}
  defaultTab={0}
  onChange={(index) => console.log('Tab changed:', index)}
/>
```

### 8. Header

```tsx
<Header title="My Application">
  <Button variant="outlined">Profile</Button>
  <Button variant="contained">Logout</Button>
</Header>
```

### 9. Footer

```tsx
<Footer />
```

### 10. ThemeToggle

```tsx
// Standalone
<ThemeToggle />

// Already included in Header component
```

### 11. LoadingSpinner

```tsx
// Basic
<LoadingSpinner />

// With message
<LoadingSpinner message="Loading data..." />

// Custom size
<LoadingSpinner size={60} />

// Full screen
<LoadingSpinner message="Please wait..." fullScreen />
```

### 12. EmptyState

```tsx
<EmptyState
  title="No items found"
  description="Get started by creating your first item"
  icon={<InboxIcon />}
  action={{
    label: 'Create Item',
    onClick: () => handleCreate()
  }}
/>
```

### 13. Breadcrumbs

```tsx
<Breadcrumbs
  items={[
    { label: 'Home', href: '/' },
    { label: 'Products', href: '/products' },
    { label: 'Details' }
  ]}
/>
```

### 14. PageContainer

```tsx
<PageContainer maxWidth="xl">
  {/* Your page content */}
</PageContainer>

// Without max width
<PageContainer maxWidth={false}>
  {/* Full width content */}
</PageContainer>
```

### 15. Section

```tsx
<Section 
  title="Section Title" 
  subtitle="Section description"
  action={<Button>Action</Button>}
  divider={true}
>
  {/* Section content */}
</Section>
```

### 16. Grid

```tsx
<Grid container spacing={3}>
  <Grid item xs={12} md={6}>
    <Card>Content 1</Card>
  </Grid>
  <Grid item xs={12} md={6}>
    <Card>Content 2</Card>
  </Grid>
</Grid>
```

## 🎨 Complete Page Example

```tsx
import { 
  PageContainer, 
  Section, 
  Breadcrumbs, 
  Card, 
  Button, 
  Input,
  Table,
  Alert 
} from '@/components/common';
import { Grid } from '@mui/material';
import { useState } from 'react';

export default function ExamplePage() {
  const [loading, setLoading] = useState(false);

  return (
    <PageContainer>
      {/* Breadcrumbs */}
      <Breadcrumbs
        items={[
          { label: 'Home', href: '/' },
          { label: 'Example Page' }
        ]}
      />

      {/* Page Header */}
      <Section 
        title="Example Page" 
        subtitle="This is an example page"
        action={<Button variant="contained">New Item</Button>}
      >
        {/* Alert */}
        <Alert severity="info" sx={{ mb: 3 }}>
          This is an informational message
        </Alert>

        {/* Form Card */}
        <Card title="Form Example" sx={{ mb: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <Input label="First Name" required />
            </Grid>
            <Grid item xs={12} md={6}>
              <Input label="Last Name" required />
            </Grid>
            <Grid item xs={12}>
              <Input label="Email" type="email" required />
            </Grid>
            <Grid item xs={12}>
              <Button 
                variant="contained" 
                loading={loading}
                onClick={() => setLoading(true)}
              >
                Submit
              </Button>
            </Grid>
          </Grid>
        </Card>

        {/* Table Card */}
        <Card title="Data Table">
          <Table
            columns={[
              { id: 'name', label: 'Name' },
              { id: 'email', label: 'Email' },
            ]}
            data={[
              { id: 1, name: 'John', email: 'john@example.com' },
              { id: 2, name: 'Jane', email: 'jane@example.com' },
            ]}
            rowKey="id"
          />
        </Card>
      </Section>
    </PageContainer>
  );
}
```

## 🎯 Custom Hooks

```tsx
import { useModal, useDebounce, useLocalStorage, useThemeMode } from '@/hooks';

// Modal management
const { isOpen, open, close } = useModal();

// Debounce search input
const [search, setSearch] = useState('');
const debouncedSearch = useDebounce(search, 500);

// LocalStorage state
const [user, setUser] = useLocalStorage('user', null);

// Theme mode
const { mode, toggleTheme } = useThemeMode();
```

## 🎨 Theme Access

```tsx
import { useTheme } from '@mui/material/styles';

function MyComponent() {
  const theme = useTheme();
  
  return (
    <div style={{ 
      backgroundColor: theme.palette.background.paper,
      color: theme.palette.text.primary 
    }}>
      Content
    </div>
  );
}
```

## 📱 Responsive Breakpoints

```tsx
import { useMediaQuery, useTheme } from '@mui/material';

function ResponsiveComponent() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const isTablet = useMediaQuery(theme.breakpoints.between('sm', 'md'));
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));

  return (
    <div>
      {isMobile && <p>Mobile view</p>}
      {isTablet && <p>Tablet view</p>}
      {isDesktop && <p>Desktop view</p>}
    </div>
  );
}
```

## 🎯 Best Practices

1. **Always use PageContainer** for page layouts
2. **Use Section** to organize content blocks
3. **Provide loading states** for async operations
4. **Show empty states** when no data
5. **Use proper semantic HTML** (h1, h2, etc.)
6. **Test in both themes** (light and dark)
7. **Ensure keyboard accessibility**
8. **Use Grid for responsive layouts**

## 📚 More Information

- Full documentation: `DESIGN_SYSTEM.md`
- Quick start guide: `QUICK_START_DESIGN.md`
- Implementation summary: `DESIGN_SYSTEM_SUMMARY.md`
- Live demos: `/design-system` and `/dashboard-example`
