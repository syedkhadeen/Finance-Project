import CustomButton from '@/components/common/Button/CustomButton';
import Input from '@/components/common/Input/Input';
import TextMd from '@/components/common/Text/TextMd';
import TextXL from '@/components/common/Text/TextXl';
import { CrossEye, Eye } from '@/constants/images.routes';
import { COMMON_MESSAGES } from '@/constants/locales';
import {
  StyledResetInputStack,
  StyledResetPasswordContainer,
  StyledResetPasswordSubContainer,
  StyledResetWidthInputStack,
} from '@/styles/resetPasswordStyle';
import { handleTogglePassword } from '@/utils/helperFunctions';
import { Stack } from '@mui/material';
import Image from 'next/image';
import { useState } from 'react';
import RegistrationLayout from '../registrationLayout/RegistrationLayout';

const ResetPassword = () => {
  const [showPassword, setShowPassword] = useState(true);
  const [confirmPassword, setConfirmPassword] = useState(true);

  return (
    <RegistrationLayout>
      <StyledResetPasswordContainer>
        <StyledResetPasswordSubContainer>
          <TextXL
            text="Reset Password"
            sx={{
              fontWeight: '400',
              lineHeight: { md: '123.5%', xs: '1.25rem' },
              letterSpacing: { md: '0.01563rem', xs: ' 0.00875rem;' },
              color: 'var(--text-black)',
              fontSize: { md: '2.125rem', xs: '1.25rem' },
            }}
          />
          <StyledResetWidthInputStack>
            <StyledResetInputStack>
              <Input
                label={COMMON_MESSAGES.Code}
                type="text"
                sx={{
                  marginTop: { md: '1.5rem', xs: '2rem' },
                }}
              />

              <Input
                endAdornment={
                  <Stack
                    onClick={() => {
                      handleTogglePassword(setShowPassword, showPassword);
                    }}
                    sx={{
                      cursor: 'pointer',
                    }}
                  >
                    {showPassword ? (
                      <Image src={CrossEye} width={20} height={20} alt="pic" />
                    ) : (
                      <Image src={Eye} width={20} height={20} alt="pic" />
                    )}
                  </Stack>
                }
                label={COMMON_MESSAGES.PASSWORD}
                type={showPassword ? 'password' : 'text'}
              />
              <Input
                endAdornment={
                  <Stack
                    onClick={() => {
                      handleTogglePassword(setConfirmPassword, confirmPassword);
                    }}
                    sx={{
                      cursor: 'pointer',
                    }}
                  >
                    {confirmPassword ? (
                      <Image src={CrossEye} width={20} height={20} alt="pic" />
                    ) : (
                      <Image src={Eye} width={20} height={20} alt="pic" />
                    )}
                  </Stack>
                }
                label={COMMON_MESSAGES.CONFIRM_PASSWORD}
                type={confirmPassword ? 'password' : 'text'}
              />
            </StyledResetInputStack>

            <CustomButton
              text={COMMON_MESSAGES.Submit}
              sx={{
                marginTop: { md: '2rem', xs: '2rem' },
                backgroundColor: 'var(--sky-blue)',
                borderRadius: '0.25rem',
                fontSize: '0.9375rem',
                lineHeight: '1.625rem',
              }}
            />

            <TextMd
              text="Resend Code"
              sx={{
                marginTop: '1.5rem',
                fontWeight: { md: '500', xs: '400' },
                lineHeight: { md: '160%', xs: '143%' },
                letterSpacing: { md: '0.00938rem', xs: '0.01063rem' },
                color: 'var(--sky-blue)',
                textAlign: 'center',
                fontSize: { md: '1.25rem', xs: '0.875rem' },
              }}
            />
          </StyledResetWidthInputStack>
        </StyledResetPasswordSubContainer>
      </StyledResetPasswordContainer>
    </RegistrationLayout>
  );
};

export default ResetPassword;
