import { Button, Stack, TextField, Typography } from '@mui/material';
import { styled } from '@mui/material/styles';

export const StyledButtonContainer = styled(Button)(({ theme }) => ({
  padding: '0.5rem 1rem',
  borderRadius: '0.25rem',
  fontSize: '0.8125rem',
  boxShadow:
    '0px 1px 5px 0px rgba(0, 0, 0, 0.12), 0px 2px 2px 0px rgba(0, 0, 0, 0.14), 0px 3px 1px -2px rgba(0, 0, 0, 0.20)',
  border: '1px solid var(--sky-blue)',
  color: 'var(--white)',
  background: 'var(--sky-blue)',
  height: '2.5rem',

  ':hover': {
    color: 'var(--white)',
    background: 'var(--sky-blue)',
  },

  '&.Mui-disabled': {
    borderColor: 'var(--sky-blue)',
    background: 'var(--sky-blue)',
    color: 'var(--white)',
    opacity: '0.5',
  },
}));

export const StyledButtonText = styled(Typography)(({ theme }) => ({
  fontSize: '0.8125rem',
  fontWeight: '500',
  fontStyle: 'normal',
  letterSpacing: '0.02875rem',
}));

export const StyledTextLg = styled(Typography)(({ theme }) => ({
  fontSize: '1.5rem',
  fontWeight: '700',
  fontStyle: 'normal',
  letterSpacing: '-0.045rem',
}));

export const StyledTextMd = styled(Typography)(({ theme }) => ({
  fontSize: '1.25rem',
  fontWeight: '500',
  fontStyle: 'normal',
}));

export const StyledTextSm = styled(Typography)(({ theme }) => ({
  fontSize: '1rem',
  fontWeight: '400',
  fontStyle: 'normal',
}));

export const StyledTextXL = styled(Typography)(({ theme }) => ({
  fontSize: '2.125rem',
  fontWeight: '700',
  fontStyle: 'normal',
  letterSpacing: '-0.045rem',
}));

export const StyledTextXs = styled(Typography)(({ theme }) => ({
  fontSize: '0.75rem',
  fontWeight: '400',
  fontStyle: 'normal',
}));

export const StyledRegistrationLayout = styled(Stack)(({ theme }) => ({
  minHeight: 'auto',
  padding: '2.77rem 2.63rem',
  alignItems: 'center',

  [theme.breakpoints.down('lg')]: {
    minHeight: '100vh',
    padding: '0rem',
  },

  [theme.breakpoints.down('sm')]: {
    padding: '0rem',
    minHeight: '100vh',
  },
}));

export const StyledRegistrationChildrenLayout = styled(Stack)(({ theme }) => ({
  flex: 1,
  width: '100%',
  justifyContent: 'center',
  alignItems: 'center',
  marginBottom: '2rem',
  minHeight: '70vh',

  [theme.breakpoints.down('lg')]: {
    minHeight: '70vh',
  },

  [theme.breakpoints.down('md')]: {
    minHeight: 'auto',
  },
}));

export const StyledRegistrationHeader = styled(Stack)(({ theme }) => ({
  justifyContent: 'flex-start',
  width: '100%',
  minHeight: '10vh',
  borderBottom: 'none',

  [theme.breakpoints.down('lg')]: {
    minHeight: '4rem',
    borderBottom: '1px solid var(--grey-100)',
  },

  [theme.breakpoints.down('xs')]: {
    minHeight: '4rem',
    borderBottom: '1px solid var(--grey-100)',
  },
}));

export const StyledInput = styled(TextField)(({ theme }) => ({
  // Styles for MUI Outlined Input
  '& .MuiOutlinedInput-root': {
    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
      borderColor: 'rgba(0, 0, 0, 0.23)',
      fontSize: '0.75rem',
      borderWidth: '0.031rem',
    },
    '&:hover .MuiOutlinedInput-notchedOutline': {
      borderColor: 'rgba(0, 0, 0, 0.23)',
      fontSize: '0.75rem',
    },
  },

  // Styles for MUI Input Label
  '& .MuiInputLabel-root': {
    '&.Mui-focused': {
      color: 'rgba(0, 0, 0, 0.60)',
      fontSize: '0.75rem',
      marginTop: '0.2rem',
      fontWeight: '400',
    },
  },
}));
