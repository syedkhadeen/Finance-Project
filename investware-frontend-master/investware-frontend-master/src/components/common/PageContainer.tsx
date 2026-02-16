import React, { ReactNode } from 'react';
import { Container, Box } from '@mui/material';

interface PageContainerProps {
  children: ReactNode;
  maxWidth?: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | false;
  disableGutters?: boolean;
}

export const PageContainer: React.FC<PageContainerProps> = ({
  children,
  maxWidth = 'xl',
  disableGutters = false,
}) => {
  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        backgroundColor: (theme) => theme.palette.background.default,
      }}
    >
      <Container
        maxWidth={maxWidth}
        disableGutters={disableGutters}
        sx={{
          flex: 1,
          py: { xs: 2, sm: 3, md: 4 },
        }}
      >
        {children}
      </Container>
    </Box>
  );
};
