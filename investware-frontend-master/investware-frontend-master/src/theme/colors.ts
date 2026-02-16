// Professional color palette - soft, neutral, and elegant
export const colors = {
  light: {
    // Background colors
    background: {
      primary: '#FFFFFF',
      secondary: '#F8F9FA',
      tertiary: '#F1F3F5',
      hover: '#E9ECEF',
    },
    // Text colors
    text: {
      primary: '#212529',
      secondary: '#495057',
      tertiary: '#6C757D',
      disabled: '#ADB5BD',
    },
    // Border colors
    border: {
      light: '#E9ECEF',
      medium: '#DEE2E6',
      dark: '#CED4DA',
    },
    // Accent colors - muted and professional
    accent: {
      primary: '#4A90E2',
      primaryHover: '#357ABD',
      secondary: '#5C6AC4',
      success: '#52C41A',
      warning: '#FAAD14',
      error: '#F5222D',
      info: '#1890FF',
    },
    // Shadow
    shadow: {
      sm: '0 1px 2px 0 rgba(0, 0, 0, 0.05)',
      md: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
      lg: '0 10px 15px -3px rgba(0, 0, 0, 0.1)',
      xl: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
    },
  },
  dark: {
    // Background colors
    background: {
      primary: '#1A1D23',
      secondary: '#22252B',
      tertiary: '#2A2D35',
      hover: '#32353D',
    },
    // Text colors
    text: {
      primary: '#E9ECEF',
      secondary: '#CED4DA',
      tertiary: '#ADB5BD',
      disabled: '#6C757D',
    },
    // Border colors
    border: {
      light: '#32353D',
      medium: '#3A3D45',
      dark: '#42454D',
    },
    // Accent colors - slightly brighter for dark mode
    accent: {
      primary: '#5B9FED',
      primaryHover: '#4A8FDD',
      secondary: '#6D7BCE',
      success: '#73D13D',
      warning: '#FFC53D',
      error: '#FF4D4F',
      info: '#40A9FF',
    },
    // Shadow
    shadow: {
      sm: '0 1px 2px 0 rgba(0, 0, 0, 0.3)',
      md: '0 4px 6px -1px rgba(0, 0, 0, 0.4)',
      lg: '0 10px 15px -3px rgba(0, 0, 0, 0.5)',
      xl: '0 20px 25px -5px rgba(0, 0, 0, 0.6)',
    },
  },
};

// Typography
export const typography = {
  fontFamily: {
    sans: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
    mono: 'ui-monospace, SFMono-Regular, "SF Mono", Menlo, Consolas, monospace',
  },
  fontSize: {
    xs: '0.75rem',    // 12px
    sm: '0.875rem',   // 14px
    base: '1rem',     // 16px
    lg: '1.125rem',   // 18px
    xl: '1.25rem',    // 20px
    '2xl': '1.5rem',  // 24px
    '3xl': '1.875rem', // 30px
    '4xl': '2.25rem', // 36px
  },
  fontWeight: {
    light: 300,
    normal: 400,
    medium: 500,
    semibold: 600,
    bold: 700,
  },
  lineHeight: {
    tight: 1.25,
    normal: 1.5,
    relaxed: 1.75,
  },
};

// Spacing
export const spacing = {
  xs: '0.25rem',   // 4px
  sm: '0.5rem',    // 8px
  md: '1rem',      // 16px
  lg: '1.5rem',    // 24px
  xl: '2rem',      // 32px
  '2xl': '3rem',   // 48px
  '3xl': '4rem',   // 64px
};

// Border radius
export const borderRadius = {
  none: '0',
  sm: '0.25rem',   // 4px
  md: '0.375rem',  // 6px
  lg: '0.5rem',    // 8px
  xl: '0.75rem',   // 12px
  full: '9999px',
};

// Transitions
export const transitions = {
  fast: '150ms cubic-bezier(0.4, 0, 0.2, 1)',
  normal: '250ms cubic-bezier(0.4, 0, 0.2, 1)',
  slow: '350ms cubic-bezier(0.4, 0, 0.2, 1)',
};
