import { Stack, Typography, Button } from '@mui/material';
import Head from 'next/head';
import Link from 'next/link';
import { useEffect } from 'react';
import { useRouter } from 'next/router';
import authService from '@/services/auth.service';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // Redirect to profile if already logged in
    if (authService.isAuthenticated()) {
      router.push('/profile');
    }
  }, []);

  const pages = [
    { name: 'Login', path: '/login' },
    { name: 'Sign Up', path: '/sign-up' },
    { name: 'Trade Calculator', path: '/trade' },
  ];

  return (
    <>
      <Head>
        <title>Investware - Bond Portfolio Management</title>
      </Head>
      <Stack sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', gap: 2 }}>
        <Typography variant="h3">Investware</Typography>
        <Typography variant="h6">Bond Portfolio Management System</Typography>
        <Stack direction="row" spacing={2} sx={{ mt: 4 }}>
          {pages.map((page) => (
            <Link key={page.path} href={page.path} passHref>
              <Button variant="contained" color="primary" size="large">
                {page.name}
              </Button>
            </Link>
          ))}
        </Stack>
        <Typography sx={{ mt: 4, color: 'gray', textAlign: 'center', maxWidth: 600 }}>
          Manage your bond portfolios, track holdings, and perform advanced bond calculations with BasisPoint integration.
        </Typography>
      </Stack>
    </>
  );
}
