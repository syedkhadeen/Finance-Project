import CustomButton from '@/components/common/Button/CustomButton';
import Input from '@/components/common/Input/Input';
import TextMd from '@/components/common/Text/TextMd';
import TextXL from '@/components/common/Text/TextXl';
import { COMMON_MESSAGES } from '@/constants/locales';
import { LOGIN } from '@/constants/routes/pages.routes';
import {
  StyledForgetPasswordContainer,
  StyledForgetPasswordSubContainer,
  StyledInputStack,
} from '@/styles/ForgetPasswordStyle';
import Link from 'next/link';
import RegistrationLayout from '../registrationLayout/RegistrationLayout';

const ForgetPassword = () => {
  return (
    <RegistrationLayout>
      <StyledForgetPasswordContainer>
        <StyledForgetPasswordSubContainer>
          <TextXL
            text="Reset Password"
            sx={{
              fontWeight: '400',
              lineHeight: '123.5%',
              letterSpacing: { md: '0.01563rem', xs: ' 0.00875rem;' },
              color: 'var(--text-black)',
              fontSize: { md: '2.125rem', xs: '1.25rem' },
            }}
          />

          <TextMd
            text="No worries, we’ll send you reset instructions."
            sx={{
              marginTop: { md: '1rem', xs: '0.5rem' },
              fontWeight: '500',
              lineHeight: ' 160%;',
              letterSpacing: '0.00938rem',
              color: 'var(--text-grey)',
              fontSize: { md: '1.25rem', xs: '0.875rem' },
            }}
          />

          <StyledInputStack>
            <Input
              label={COMMON_MESSAGES.EMAIL_ADDRESS}
              type="email"
              sx={{
                marginTop: { md: '1.5rem', xs: '2rem' },
              }}
            />

            <CustomButton
              text={COMMON_MESSAGES.SEND}
              sx={{
                marginTop: { md: '2rem', xs: '2rem' },
                backgroundColor: 'var(--sky-blue)',
                borderRadius: '0.25rem',
                fontSize: '0.9375rem',
                lineHeight: '1.625rem',
              }}
            />

            <Link href={LOGIN}>
              <TextMd
                text="Back to Login"
                sx={{
                  marginTop: '1.5rem',
                  fontWeight: { md: '500', xs: '400' },
                  lineHeight: { md: 's160%', xs: '143%' },
                  letterSpacing: { md: '0.00938rem', xs: '0.01063rem' },
                  color: 'var(--text-grey)',
                  textAlign: 'center',
                  fontSize: { md: '1.25rem', xs: '0.875rem' },
                }}
              />
            </Link>
          </StyledInputStack>
        </StyledForgetPasswordSubContainer>
      </StyledForgetPasswordContainer>
    </RegistrationLayout>
  );
};

export default ForgetPassword;
