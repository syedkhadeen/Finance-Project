import { Logo } from '@/constants/images.routes';
import { StyledHeaderContainer, StyledRegistrationChildrenLayout } from '@/styles/signUpStyle';
import { Stack } from '@mui/material';
import Image from 'next/image';

const Header = () => {
  return (
    <StyledRegistrationChildrenLayout>
      <Stack
        sx={{
          padding: { lg: '0rem', xs: '1rem 1.5rem' },
        }}
      >
        <StyledHeaderContainer>
          <Image priority src={Logo} alt={'icon'} fill />
        </StyledHeaderContainer>
      </Stack>
    </StyledRegistrationChildrenLayout>
  );
};

export default Header;
