import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import Head from 'next/head';
import TradeForm from '@/components/ui/trade/TradeForm';
import authService from '@/services/auth.service';
import { Box, CircularProgress } from '@mui/material';

export default function TradePage() {
  const router = useRouter();
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    if (!authService.isAuthenticated()) {
      router.push('/login');
    } else {
      setChecking(false);
    }
  }, [router]);

  if (checking) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="80vh"
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <>
      <Head>
        <title>Invest Ware - Trade Input</title>
      </Head>
      <TradeForm />
    </>
  );
}