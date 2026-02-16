import { createTheme, ThemeOptions } from '@mui/material/styles';
import { Roboto } from 'next/font/google';
import { colors } from './colors';

export const roboto = Roboto({
  weight: ['300', '400', '500', '700'],
  subsets: ['latin'],
  display: 'swap',
});

export const createAppTheme = (mode: 'light' | 'dark') => {
  const palette = mode === 'light' ? colors.light : colors.dark;

  const themeOptions: ThemeOptions = {
    palette: {
      mode,
      primary: {
        main: palette.accent.primary,
        dark: palette.accent.primaryHover,
      },
      secondary: {
        main: palette.accent.secondary,
      },
      success: {
        main: palette.accent.success,
      },
      warning: {
        main: palette.accent.warning,
      },
      error: {
        main: palette.accent.error,
      },
      info: {
        main: palette.accent.info,
      },
      background: {
        default: palette.background.primary,
        paper: palette.background.secondary,
      },
      text: {
        primary: palette.text.primary,
        secondary: palette.text.secondary,
        disabled: palette.text.disabled,
      },
      divider: palette.border.medium,
    },
    typography: {
      fontFamily: roboto.style.fontFamily,
      button: {
        textTransform: 'none',
        fontWeight: 500,
      },
      h1: {
        fontSize: '2.25rem',
        fontWeight: 600,
        lineHeight: 1.25,
      },
      h2: {
        fontSize: '1.875rem',
        fontWeight: 600,
        lineHeight: 1.3,
      },
      h3: {
        fontSize: '1.5rem',
        fontWeight: 600,
        lineHeight: 1.35,
      },
      h4: {
        fontSize: '1.25rem',
        fontWeight: 600,
        lineHeight: 1.4,
      },
      h5: {
        fontSize: '1.125rem',
        fontWeight: 600,
        lineHeight: 1.45,
      },
      h6: {
        fontSize: '1rem',
        fontWeight: 600,
        lineHeight: 1.5,
      },
      body1: {
        fontSize: '1rem',
        lineHeight: 1.5,
      },
      body2: {
        fontSize: '0.875rem',
        lineHeight: 1.5,
      },
    },
    shape: {
      borderRadius: 6,
    },
    components: {
      MuiButton: {
        styleOverrides: {
          root: {
            borderRadius: 6,
            padding: '8px 16px',
            fontSize: '0.875rem',
            fontWeight: 500,
            boxShadow: 'none',
            transition: 'all 0.2s ease',
            '&:hover': {
              boxShadow: palette.shadow.sm,
            },
          },
          contained: {
            '&:hover': {
              boxShadow: palette.shadow.md,
            },
          },
        },
      },
      MuiCard: {
        styleOverrides: {
          root: {
            borderRadius: 8,
            boxShadow: palette.shadow.sm,
            border: `1px solid ${palette.border.light}`,
            transition: 'all 0.2s ease',
            '&:hover': {
              boxShadow: palette.shadow.md,
            },
          },
        },
      },
      MuiTextField: {
        styleOverrides: {
          root: {
            '& .MuiOutlinedInput-root': {
              borderRadius: 6,
              transition: 'all 0.2s ease',
              '&:hover': {
                '& .MuiOutlinedInput-notchedOutline': {
                  borderColor: palette.border.dark,
                },
              },
            },
          },
        },
      },
      MuiPaper: {
        styleOverrides: {
          root: {
            backgroundImage: 'none',
          },
          elevation1: {
            boxShadow: palette.shadow.sm,
          },
          elevation2: {
            boxShadow: palette.shadow.md,
          },
          elevation3: {
            boxShadow: palette.shadow.lg,
          },
        },
      },
      MuiAlert: {
        styleOverrides: {
          root: {
            borderRadius: 6,
          },
        },
      },
    },
  };

  return createTheme(themeOptions);
};

// Default light theme
const theme = createAppTheme('light');

export default theme;
