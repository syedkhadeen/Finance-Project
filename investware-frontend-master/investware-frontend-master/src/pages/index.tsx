import { Stack, Typography, Button } from '@mui/material';
import Head from 'next/head';
import Link from 'next/link';

export default function Home() {
  const pages = [
    { name: 'Login', path: '/login' },
    { name: 'Sign Up', path: '/sign-up' },
    { name: 'Trade Input Screen', path: '/trade' },
    { name: 'Forget Password', path: '/forget-password' },
    { name: 'Reset Password', path: '/reset-password' },
  ];

  return (
    <>
      <Head>
        <title>Invest Ware - Navigation</title>
      </Head>
      <Stack sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', gap: 2 }}>
        <Typography variant="h3">Invest Ware Prototype</Typography>
        <Typography variant="h6">Available Frontend Pages:</Typography>
        <Stack direction="row" spacing={2}>
          {pages.map((page) => (
            <Link key={page.path} href={page.path} passHref>
              <Button variant="contained" color="primary">
                {page.name}
              </Button>
            </Link>
          ))}
        </Stack>
        <Typography sx={{ mt: 4, color: 'gray' }}>
          Note: Use the "Trade Input Screen" to see the new dashboard page.
        </Typography>
      </Stack>
    </>
  );
}
