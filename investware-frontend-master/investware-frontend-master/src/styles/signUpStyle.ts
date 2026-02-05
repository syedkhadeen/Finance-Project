import { Stack, Typography } from '@mui/material';
import { styled } from '@mui/material/styles';

export const StyledSignUpContainer = styled(Stack)(({ theme }) => ({
  justifyContent: 'center',
  alignItems: 'center',
  height: '100%',
}));

export const StyledTerms = styled(Typography)(({ theme }) => ({
  fontSize: '1rem',
  fontWeight: '400',
  fontStyle: 'normal',
  lineHeight: '150%',
  letterSpacing: '0.00938',
  color: 'var(--text-black)',
  marginLeft: '0.5rem',
  '& span': {
    color: 'var(--sky-blue)',
  },
}));

export const StyledSignUpSuccessSubContainer = styled(Stack)(({ theme }) => ({
  position: 'relative',
  width: '22.125rem',
  height: '16.3125rem',
  maxWidth: '22.125rem',
  maxHeight: '16.3125rem',
  [`@media (max-width: ${theme.breakpoints.values.md}px)`]: {
    maxWidth: '17.8125rem',
    maxHeight: '13.5625rem',
  },
  [`@media (max-width: ${theme.breakpoints.values.xs}px)`]: {
    maxWidth: '10.5625rem',
    maxHeight: '8.5625rem',
  },
  '& img': {
    position: 'absolute',
    cursor: 'pointer',
    width: '100%',
    height: '100%',
  },
}));

export const StyledRegistrationChildrenLayout = styled(Stack)(({ theme }) => ({
  justifyContent: 'flex-start',
  width: '100%',
  minHeight: '10vh',
  borderBottom: 'none',

  [theme.breakpoints.down('lg')]: {
    minHeight: '4rem',
    borderBottom: '1px solid var(--grey-100)',
  },
}));

export const StyledHeaderContainer = styled(Stack)(({ theme }) => ({
  position: 'relative',
  width: '10rem',
  height: '2.25rem',
  [`@media (max-width: ${theme.breakpoints.values.sm}px)`]: {
    width: '8.875rem',
    height: '2rem',
  },
  '& img': {
    position: 'absolute',
    cursor: 'pointer',
    width: '100%',
    height: '100%',
  },
}));

export const StyledSignUpFormSubContainer = styled(Stack)(({ theme }) => ({
  width: '46.5rem',
  borderRadius: '0.625rem',
  padding: '2.5rem 1.875rem',
  boxShadow: '0px 4px 50px 0px rgba(0, 0, 0, 0.10)',

  [theme.breakpoints.down('lg')]: {
    width: '100%',
  },

  [theme.breakpoints.down('md')]: {
    padding: '0rem',
    boxShadow: 'none',
  },
  boxSizing: 'border-box',
}));

export const StyledSignUpFormSubMiniContainer = styled(Stack)(({ theme }) => ({
  boxShadow: 'none',
  padding: '1rem 1rem 1rem 1rem',
}));

export const StyledSignUpText = styled(Stack)(({ theme }) => ({
  fontSize: '1.5rem',
  fontStyle: 'normal',
  letterSpacing: '-0.045rem',
  fontWeight: '400',
  lineHeight: '133.4%',
  color: 'var(--text-black)',
  textAlign: 'left',
  [theme.breakpoints.down('sm')]: {
    textAlign: 'center',
  },
}));

export const StyledInputContainer = styled(Stack)(({ theme }) => ({
  marginTop: '2.25rem',
  gap: '1rem',
}));

export const StyledTwoInput = styled(Stack)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'row',
  justifyContent: 'space-between',
  gap: '0rem',
  [theme.breakpoints.down('sm')]: {
    gap: '1rem',
    flexDirection: 'column',
  },
}));

export const StyledHalfInput = styled(Stack)(({ theme }) => ({
  width: '48%',
  [theme.breakpoints.down('sm')]: {
    width: '100%',
  },
}));

export const StyledFormContainer = styled(Stack)(({ theme }) => ({
  marginTop: '2.06rem',
  marginLeft: '1rem',
}));

export const StyledSignUpSuccessContainer = styled(Stack)(({ theme }) => ({
  alignItems: 'center',
}));
