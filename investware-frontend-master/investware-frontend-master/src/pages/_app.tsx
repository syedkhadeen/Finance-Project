import Layout from '@/components/ui/Layout/Layout';
import '@/styles/globals.css';
import { createAppTheme } from '@/theme/theme';
import type { AppProps } from 'next/app';
import { ThemeProvider as StyledThemeProvider } from 'styled-components';
import { ThemeProvider as MuiThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { ThemeModeProvider, useThemeMode } from '@/contexts/ThemeContext';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useMemo } from 'react';

function AppContent({ Component, pageProps }: AppProps) {
  const { mode } = useThemeMode();
  const theme = useMemo(() => createAppTheme(mode), [mode]);

  return (
    <MuiThemeProvider theme={theme}>
      <StyledThemeProvider theme={theme}>
        <CssBaseline />
        <ToastContainer
          position="top-right"
          autoClose={3000}
          hideProgressBar={false}
          newestOnTop
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme={mode}
        />
        <Layout>
          <Component {...pageProps} />
        </Layout>
      </StyledThemeProvider>
    </MuiThemeProvider>
  );
}

export default function App(props: AppProps) {
  return (
    <ThemeModeProvider>
      <AppContent {...props} />
    </ThemeModeProvider>
  );
}
