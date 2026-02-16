# Quick Start Guide - Design System

## 🚀 Getting Started

Your new design system is ready to use! Here's everything you need to know.

## ✨ What's New

### 1. Light & Dark Mode
- Automatic theme switching with smooth transitions
- Theme preference saved to localStorage
- Respects system preferences
- Toggle button in header

### 2. Professional Color Palette
- Soft grays and whites for light mode
- Dark backgrounds with proper contrast for dark mode
- Muted accent colors (#4A90E2 primary)
- No flashy or neon colors

### 3. Reusable Components
All components are in `src/components/common/`:
- Button, Input, Card, Modal, Table
- Header, Footer, ThemeToggle
- LoadingSpinner, EmptyState, Alert
- Breadcrumbs, Tabs, Section, PageContainer

## 📖 Usage Examples

### Basic Page Structure

```tsx
import { PageContainer, Section, Breadcrumbs } from '@/components/common';

export default function MyPage() {
  return (
    <PageContainer>
      <Breadcrumbs items={[
        { label: 'Home', href: '/' },
        { label: 'My Page' }
      ]} />
      
      <Section title="Page Title" subtitle="Description">
        {/* Your content here */}
      </Section>
    </PageContainer>
  );
}
```

### Using Components

```tsx
import { Button, Input, Card, Table } from '@/components/common';

// Button with loading state
<Button variant="contained" loading={isLoading}>
  Save Changes
</Button>

// Input field
<Input 
  label="Email" 
  type="email" 
  required 
  helperText="Enter your email address"
/>

// Card with actions
<Card 
  title="User Profile" 
  subtitle="Manage your account"
  actions={<Button>Edit</Button>}
>
  <p>Card content</p>
</Card>

// Data table
<Table 
  columns={columns} 
  data={data} 
  rowKey="id"
  page={page}
  onPageChange={setPage}
/>
```

### Theme Toggle

The theme toggle is automatically included in the Header component:

```tsx
import { Header } from '@/components/common';

<Header title="My App">
  {/* Additional header content */}
</Header>
```

Or use it standalone:

```tsx
import { ThemeToggle } from '@/components/common';

<ThemeToggle />
```

### Accessing Theme Mode

```tsx
import { useThemeMode } from '@/contexts/ThemeContext';

function MyComponent() {
  const { mode, toggleTheme } = useThemeMode();
  
  return (
    <div>
      Current theme: {mode}
      <button onClick={toggleTheme}>Toggle</button>
    </div>
  );
}
```

## 🎨 Demo Pages

Visit these pages to see the design system in action:

1. **Design System Demo**: `/design-system`
   - Complete component showcase
   - Interactive examples
   - All variants and states

2. **Dashboard Example**: `/dashboard-example`
   - Real-world application
   - Stats cards, tables, actions
   - Professional layout

## 📁 File Structure

```
src/
├── components/
│   └── common/              # All reusable components
│       ├── Button.tsx
│       ├── Input.tsx
│       ├── Card.tsx
│       ├── Modal.tsx
│       ├── Table.tsx
│       ├── Header.tsx
│       ├── Footer.tsx
│       ├── ThemeToggle.tsx
│       ├── LoadingSpinner.tsx
│       ├── EmptyState.tsx
│       ├── Alert.tsx
│       ├── Breadcrumbs.tsx
│       ├── Tabs.tsx
│       ├── Section.tsx
│       ├── PageContainer.tsx
│       ├── Grid.tsx
│       └── index.ts         # Barrel export
├── contexts/
│   └── ThemeContext.tsx     # Theme state management
├── theme/
│   ├── colors.ts            # Color definitions
│   └── theme.ts             # MUI theme configuration
├── styles/
│   └── globals.css          # Global styles with CSS variables
└── pages/
    ├── _app.tsx             # Updated with theme providers
    ├── design-system.tsx    # Component showcase
    └── dashboard-example.tsx # Real-world example
```

## 🎯 Key Features

### Responsive Design
All components are fully responsive:
- Mobile-first approach
- Breakpoints: xs, sm, md, lg, xl
- Flexible grid system

### Accessibility
- Proper ARIA labels
- Keyboard navigation
- Focus states
- Screen reader support
- Semantic HTML

### Performance
- Optimized re-renders
- Smooth animations (0.2s transitions)
- Lazy loading support
- Minimal bundle size

## 🛠️ Customization

### Colors
Edit `src/theme/colors.ts` to customize the color palette:

```typescript
export const colors = {
  light: {
    accent: {
      primary: '#4A90E2',  // Change this
      // ...
    }
  }
}
```

### Typography
Edit `src/theme/theme.ts` to customize fonts and sizes:

```typescript
typography: {
  fontFamily: 'Your Font, sans-serif',
  h1: { fontSize: '2.5rem' },
  // ...
}
```

### Spacing
Edit `src/theme/colors.ts` spacing section:

```typescript
export const spacing = {
  xs: '0.25rem',
  sm: '0.5rem',
  // ...
}
```

## 📝 Best Practices

1. **Always use the common components** instead of MUI directly
2. **Use PageContainer** for consistent page layouts
3. **Use Section** for organizing content blocks
4. **Provide loading states** for async operations
5. **Show empty states** when no data is available
6. **Use proper semantic HTML** (h1, h2, etc.)
7. **Test in both light and dark modes**
8. **Ensure keyboard accessibility**

## 🔄 Migration Guide

To update existing pages:

### Before:
```tsx
import { Container, Box, Typography } from '@mui/material';

export default function OldPage() {
  return (
    <Container>
      <Box sx={{ py: 4 }}>
        <Typography variant="h4">Title</Typography>
        {/* content */}
      </Box>
    </Container>
  );
}
```

### After:
```tsx
import { PageContainer, Section } from '@/components/common';

export default function NewPage() {
  return (
    <PageContainer>
      <Section title="Title">
        {/* content */}
      </Section>
    </PageContainer>
  );
}
```

## 🐛 Troubleshooting

### Theme not switching?
- Check that ThemeModeProvider wraps your app in `_app.tsx`
- Clear localStorage and refresh

### Components not styled correctly?
- Ensure CssBaseline is included in `_app.tsx`
- Check that MuiThemeProvider wraps your app

### TypeScript errors?
- Run `npm install` to ensure all dependencies are installed
- Restart your TypeScript server

## 📚 Additional Resources

- Full documentation: `DESIGN_SYSTEM.md`
- Material-UI docs: https://mui.com
- Component examples: `/design-system` page

## 🎉 You're Ready!

Start building beautiful, professional interfaces with your new design system. All components are production-ready and fully typed with TypeScript.

Happy coding! 🚀
