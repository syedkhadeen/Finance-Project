import { Stack } from '@mui/material';
import { styled } from '@mui/material/styles';

export const StyledForgetPasswordContainer = styled(Stack)(({ theme }) => ({
  width: '46.5rem',
  borderRadius: '0.625rem',
  boxShadow: '0px 4px 50px 0px rgba(0, 0, 0, 0.10)',

  [theme.breakpoints.down('md')]: {
    width: '100%',
    boxShadow: 'none',
  },
}));

export const StyledForgetPasswordSubContainer = styled(Stack)(({ theme }) => ({
  alignItems: 'center',
  padding: '2.5rem 1.875rem',

  [theme.breakpoints.down('md')]: {
    padding: '2.5rem 1.875rem 0rem 1.875rem',
  },
}));

export const StyledInputStack = styled(Stack)(({ theme }) => ({
  width: '28.5625rem',
  [theme.breakpoints.down('md')]: {
    width: '100%',
  },
}));
