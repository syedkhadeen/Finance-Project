# Design System Documentation

## Overview

This design system provides a comprehensive set of reusable React components built with Material-UI, featuring a professional, minimalistic design with full light/dark mode support.

## Key Features

- ✨ **Light & Dark Mode**: Seamless theme switching with smooth transitions
- 🎨 **Professional Color Palette**: Soft grays, whites, and muted accents
- 📱 **Fully Responsive**: Works perfectly on desktop, tablet, and mobile
- ♿ **Accessible**: WCAG compliant with proper focus states and ARIA labels
- 🧩 **Reusable Components**: Consistent, well-documented components
- 🎯 **TypeScript**: Full type safety throughout

## Getting Started

### Theme Context

The theme system is managed through React Context. The theme mode is automatically saved to localStorage and respects system preferences.

```tsx
import { useThemeMode } from '@/contexts/ThemeContext';

function MyComponent() {
  const { mode, toggleTheme } = useThemeMode();
  
  return (
    <button onClick={toggleTheme}>
      Current mode: {mode}
    </button>
  );
}
```

### Color System

Colors are defined in `src/theme/colors.ts` with separate palettes for light and dark modes:

**Light Mode:**
- Background: `#FFFFFF`, `#F8F9FA`, `#F1F3F5`
- Text: `#212529`, `#495057`, `#6C757D`
- Primary Accent: `#4A90E2`

**Dark Mode:**
- Background: `#1A1D23`, `#22252B`, `#2A2D35`
- Text: `#E9ECEF`, `#CED4DA`, `#ADB5BD`
- Primary Accent: `#5B9FED`

## Components

### Button

Professional button component with loading states and variants.

```tsx
import { Button } from '@/components/common';

<Button variant="contained">Primary</Button>
<Button variant="outlined">Outlined</Button>
<Button variant="text">Text</Button>
<Button variant="contained" loading={true}>Loading</Button>
<Button startIcon={<AddIcon />}>With Icon</Button>
```

**Props:**
- All Material-UI Button props
- `loading?: boolean` - Shows loading spinner

### Input

Consistent text input with proper styling.

```tsx
import { Input } from '@/components/common';

<Input label="Email" placeholder="Enter email..." />
<Input label="Password" type="password" required />
<Input label="Search" InputProps={{ startAdornment: <SearchIcon /> }} />
```

**Props:**
- All Material-UI TextField props

### Card

Container component for grouping related content.

```tsx
import { Card } from '@/components/common';

<Card title="Card Title" subtitle="Subtitle">
  <p>Card content goes here</p>
</Card>

<Card 
  title="With Actions"
  actions={<Button>Action</Button>}
  headerAction={<IconButton><MoreIcon /></IconButton>}
>
  Content
</Card>
```

**Props:**
- `title?: string` - Card title
- `subtitle?: string` - Card subtitle
- `actions?: ReactNode` - Footer actions
- `headerAction?: ReactNode` - Header action button
- All Material-UI Card props

### Modal

Dialog component for overlays and confirmations.

```tsx
import { Modal } from '@/components/common';

<Modal
  open={isOpen}
  onClose={handleClose}
  title="Modal Title"
  actions={
    <>
      <Button onClick={handleClose}>Cancel</Button>
      <Button variant="contained" onClick={handleConfirm}>Confirm</Button>
    </>
  }
>
  <p>Modal content</p>
</Modal>
```

**Props:**
- `open: boolean` - Controls visibility
- `onClose: () => void` - Close handler
- `title?: string` - Modal title
- `actions?: ReactNode` - Footer actions
- `maxWidth?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'`
- `fullWidth?: boolean`

### Table

Data table with pagination and custom formatting.

```tsx
import { Table } from '@/components/common';

const columns = [
  { id: 'name', label: 'Name', minWidth: 150 },
  { id: 'email', label: 'Email', minWidth: 200 },
  { 
    id: 'status', 
    label: 'Status',
    format: (value) => <Chip label={value} />
  },
];

<Table
  columns={columns}
  data={data}
  page={page}
  rowsPerPage={10}
  totalCount={100}
  onPageChange={setPage}
  onRowsPerPageChange={setRowsPerPage}
  rowKey="id"
/>
```

**Props:**
- `columns: Column[]` - Column definitions
- `data: T[]` - Table data
- `page?: number` - Current page
- `rowsPerPage?: number` - Rows per page
- `totalCount?: number` - Total items
- `onPageChange?: (page: number) => void`
- `onRowsPerPageChange?: (rows: number) => void`
- `emptyMessage?: string`
- `rowKey?: keyof T | ((row: T) => string | number)`

### Alert

Notification component for messages.

```tsx
import { Alert } from '@/components/common';

<Alert severity="success" title="Success">Operation completed</Alert>
<Alert severity="error">An error occurred</Alert>
<Alert severity="warning" title="Warning">Please review</Alert>
<Alert severity="info">Information message</Alert>
```

**Props:**
- `severity: 'success' | 'error' | 'warning' | 'info'`
- `title?: string`
- All Material-UI Alert props

### Tabs

Tabbed interface for organizing content.

```tsx
import { Tabs } from '@/components/common';

<Tabs
  tabs={[
    { label: 'Tab 1', content: <div>Content 1</div> },
    { label: 'Tab 2', content: <div>Content 2</div> },
    { label: 'Disabled', content: <div>Content 3</div>, disabled: true },
  ]}
  defaultTab={0}
  onChange={(index) => console.log(index)}
/>
```

**Props:**
- `tabs: TabItem[]` - Tab definitions
- `defaultTab?: number` - Initial tab index
- `onChange?: (index: number) => void`

### Header

Application header with theme toggle.

```tsx
import { Header } from '@/components/common';

<Header title="My App">
  <Button>Custom Action</Button>
</Header>
```

**Props:**
- `title?: string` - App title
- `children?: ReactNode` - Additional header content

### Footer

Application footer with links.

```tsx
import { Footer } from '@/components/common';

<Footer />
```

### LoadingSpinner

Loading indicator with optional message.

```tsx
import { LoadingSpinner } from '@/components/common';

<LoadingSpinner message="Loading..." />
<LoadingSpinner size={60} fullScreen />
```

**Props:**
- `message?: string` - Loading message
- `size?: number` - Spinner size
- `fullScreen?: boolean` - Full screen overlay

### EmptyState

Empty state placeholder.

```tsx
import { EmptyState } from '@/components/common';

<EmptyState
  title="No items found"
  description="Get started by creating your first item"
  icon={<InboxIcon />}
  action={{
    label: 'Create Item',
    onClick: handleCreate
  }}
/>
```

**Props:**
- `title: string` - Main message
- `description?: string` - Supporting text
- `icon?: ReactNode` - Custom icon
- `action?: { label: string; onClick: () => void }`

### Breadcrumbs

Navigation breadcrumbs.

```tsx
import { Breadcrumbs } from '@/components/common';

<Breadcrumbs
  items={[
    { label: 'Home', href: '/' },
    { label: 'Products', href: '/products' },
    { label: 'Details' },
  ]}
/>
```

**Props:**
- `items: BreadcrumbItem[]` - Breadcrumb items

### ThemeToggle

Theme switcher button.

```tsx
import { ThemeToggle } from '@/components/common';

<ThemeToggle />
```

## Design Principles

1. **Simplicity**: Clean, uncluttered interfaces
2. **Consistency**: Uniform spacing, typography, and colors
3. **Accessibility**: Keyboard navigation and screen reader support
4. **Performance**: Optimized rendering and smooth animations
5. **Responsiveness**: Mobile-first, adaptive layouts

## Typography Scale

- **H1**: 2.25rem (36px) - Page titles
- **H2**: 1.875rem (30px) - Section headers
- **H3**: 1.5rem (24px) - Subsection headers
- **H4**: 1.25rem (20px) - Card titles
- **H5**: 1.125rem (18px) - Small headers
- **H6**: 1rem (16px) - Smallest headers
- **Body1**: 1rem (16px) - Primary text
- **Body2**: 0.875rem (14px) - Secondary text

## Spacing System

- **xs**: 4px
- **sm**: 8px
- **md**: 16px
- **lg**: 24px
- **xl**: 32px
- **2xl**: 48px
- **3xl**: 64px

## Border Radius

- **sm**: 4px - Small elements
- **md**: 6px - Buttons, inputs
- **lg**: 8px - Cards
- **xl**: 12px - Large containers

## Shadows

- **sm**: Subtle elevation
- **md**: Standard elevation
- **lg**: Prominent elevation
- **xl**: Maximum elevation

## Demo Page

Visit `/design-system` to see all components in action with interactive examples.

## Best Practices

1. **Use semantic HTML**: Proper heading hierarchy, buttons vs links
2. **Maintain contrast ratios**: Ensure text is readable in both themes
3. **Test keyboard navigation**: All interactive elements should be accessible
4. **Provide loading states**: Use loading spinners for async operations
5. **Show empty states**: Guide users when no data is available
6. **Use consistent spacing**: Follow the spacing system
7. **Optimize images**: Use appropriate formats and sizes
8. **Handle errors gracefully**: Show clear error messages

## File Structure

```
src/
├── components/
│   └── common/           # Reusable components
│       ├── Button.tsx
│       ├── Input.tsx
│       ├── Card.tsx
│       ├── Modal.tsx
│       ├── Table.tsx
│       ├── Header.tsx
│       ├── Footer.tsx
│       └── index.ts      # Barrel export
├── contexts/
│   └── ThemeContext.tsx  # Theme management
├── theme/
│   ├── colors.ts         # Color definitions
│   └── theme.ts          # MUI theme config
└── styles/
    └── globals.css       # Global styles
```

## Contributing

When adding new components:

1. Follow existing patterns and naming conventions
2. Include TypeScript types for all props
3. Support both light and dark modes
4. Add hover and focus states
5. Ensure responsive behavior
6. Document props and usage
7. Add to the design system demo page
