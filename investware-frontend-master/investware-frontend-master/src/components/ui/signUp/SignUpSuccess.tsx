import CustomButton from '@/components/common/Button/CustomButton';
import TextLg from '@/components/common/Text/TextLg';
import { Message } from '@/constants/images.routes';
import { COMMON_MESSAGES } from '@/constants/locales';
import { LOGIN } from '@/constants/routes/pages.routes';
import { StyledSignUpSuccessContainer, StyledSignUpSuccessSubContainer } from '@/styles/signUpStyle';
import { Stack } from '@mui/material';
import Image from 'next/image';
import Link from 'next/link';

const SignUpSuccess = () => {
  return (
    <StyledSignUpSuccessContainer>
      <StyledSignUpSuccessSubContainer>
        <Image priority src={Message} alt={'icon'} fill />
      </StyledSignUpSuccessSubContainer>
      <Stack
        sx={{
          alignItems: 'center',
          width: '100%',
        }}
      >
        <TextLg
          text="Email Verified"
          sx={{
            marginTop: { lg: '3.87rem', sm: '2rem', xs: '0.69rem' },
            fontWeight: { sm: '400', xs: '500' },
            lineHeight: { lg: '123.5%', xs: '160%' },
            color: 'var(--text-black)',
            fontSize: { lg: '2.125rem', sm: '1.5rem', xs: '1.25rem' },
            letterSpacing: '0px',
          }}
        />
        <TextLg
          text="“Your email has been verified”"
          sx={{
            marginTop: { lg: '2.13rem', xs: '1rem' },
            fontWeight: { lg: '400', sm: '500', xs: '500' },
            lineHeight: { lg: '133.4%', xs: '143%' },
            color: 'var(--text-grey)',
            fontSize: { lg: '1.5rem', sm: '1rem', xs: '0.875rem' },
            letterSpacing: '0px',
          }}
        />
        <Link href={LOGIN}>
          <CustomButton
            text={COMMON_MESSAGES.LOGIN}
            sx={{
              marginTop: { lg: '1.5rem', sm: '3rem', xs: '1.63rem' },
              backgroundColor: 'var(--sky-blue)',
              width: '21.4375rem',
              borderRadius: '0.25rem',
              padding: '0.5rem 1.375rem',
            }}
          />
        </Link>
      </Stack>
    </StyledSignUpSuccessContainer>
  );
};

export default SignUpSuccess;
