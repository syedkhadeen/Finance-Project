import { ReactNode } from 'react';
import { Box } from '@mui/material';
import { Navbar } from '@/components/common/Navbar';
import { Footer } from '@/components/common/Footer';

interface Props {
  children: ReactNode;
}

const Layout = ({ children }: Props) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        backgroundColor: (theme) => theme.palette.background.default,
      }}
    >
      <Navbar />
      <Box
        component="main"
        sx={{
          flex: 1,
          width: '100%',
        }}
      >
        {children}
      </Box>
      <Footer />
    </Box>
  );
};

export default Layout;
