import { Stack } from '@mui/material';
import { styled } from '@mui/material/styles';

export const StyledLoginContainer = styled(Stack)(({ theme }) => ({
  width: '46.5rem',

  [theme.breakpoints.down('md')]: {
    width: '100%',
  },
}));

export const StyledLoginSubContainer = styled(Stack)(({ theme }) => ({
  padding: '4rem 5.44rem',
  borderRadius: '0.625rem',
  boxShadow: '0px 4px 50px 0px rgba(0, 0, 0, 0.10)',

  [theme.breakpoints.down('md')]: {
    boxShadow: 'none',
    padding: '2rem 1rem 0rem 1rem',
  },
}));

export const StyledInputBox = styled(Stack)(({ theme }) => ({
  marginTop: '1.94rem',
  gap: '1.06rem',
}));

export const StyledFormControlBox = styled(Stack)(({ theme }) => ({
  marginTop: '2.06rem',
  marginLeft: '1rem',
}));

export const StyledBottomLink = styled(Stack)(({ theme }) => ({
  marginTop: '1.12rem',
  justifyContent: 'space-between',
  display: 'flex',
  flexDirection: 'row',
}));
