import CustomButton from '@/components/common/Button/CustomButton';
import Input from '@/components/common/Input/Input';
import { Check, CrossEye, Eye, UnCheck } from '@/constants/images.routes';
import { COMMON_MESSAGES, SIGNUP_PAGE_MESSAGES } from '@/constants/locales';
import {
  StyledFormContainer,
  StyledHalfInput,
  StyledInputContainer,
  StyledSignUpFormSubContainer,
  StyledSignUpFormSubMiniContainer,
  StyledSignUpText,
  StyledTerms,
  StyledTwoInput,
} from '@/styles/signUpStyle';
import { handleTogglePassword } from '@/utils/helperFunctions';
import { Checkbox, FormControlLabel, Stack } from '@mui/material';
import Image from 'next/image';
import { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';

import { useRouter } from 'next/router';

interface Props {
  setSignUpSuccess: (value: boolean) => void;
}

const SignUpForm = ({ setSignUpSuccess }: Props) => {
  const router = useRouter();
  const [showPassword, setShowPassword] = useState(true);
  const [confirmPassword, setConfirmPassword] = useState(true);
  const [checkbox, setCheckbox] = useState(false);
  const [loading, setLoading] = useState(false);

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    companyName: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const handleInputChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [field]: e.target.value });
  };

  const handleCheckboxChange = () => {
    setCheckbox(!checkbox);
  };

  const handleSubmit = async () => {
    if (!checkbox) {
      toast.error('Please accept the terms and conditions');
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }

    setLoading(true);
    try {
      const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3002';
      // The backend expects 'auth_id'. We'll send the email as the auth_id for now.
      await axios.post(`${apiUrl}/v1/api/auth/signup`, {
        auth_id: formData.email,
      });

      toast.success('Sign up successful!');
      router.push('/login');
    } catch (error: any) {
      console.error('Signup error:', error);
      toast.error(error.response?.data?.message || 'Failed to sign up');
    } finally {
      setLoading(false);
    }
  };

  return (
    <StyledSignUpFormSubContainer>
      <StyledSignUpFormSubMiniContainer>
        <StyledSignUpText>Sign up</StyledSignUpText>

        <StyledInputContainer>
          <StyledTwoInput>
            <StyledHalfInput>
              <Input label={COMMON_MESSAGES.FIRST_NAME} value={formData.firstName} onChange={handleInputChange('firstName')} />
            </StyledHalfInput>
            <StyledHalfInput>
              <Input label={COMMON_MESSAGES.LAST_NAME} value={formData.lastName} onChange={handleInputChange('lastName')} />
            </StyledHalfInput>
          </StyledTwoInput>
          <Stack>
            <Input label={COMMON_MESSAGES.COMPANY_NAME} value={formData.companyName} onChange={handleInputChange('companyName')} />
          </Stack>
          <Stack>
            <Input label={COMMON_MESSAGES.EMAIL_ADDRESS} type="email" value={formData.email} onChange={handleInputChange('email')} />
          </Stack>
          <StyledTwoInput>
            <StyledHalfInput>
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
                value={formData.password}
                onChange={handleInputChange('password')}
              />
            </StyledHalfInput>
            <StyledHalfInput>
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
                value={formData.confirmPassword}
                onChange={handleInputChange('confirmPassword')}
              />
            </StyledHalfInput>
          </StyledTwoInput>
        </StyledInputContainer>

        <StyledFormContainer>
          <FormControlLabel
            control={
              <Checkbox
                checked={checkbox}
                onChange={handleCheckboxChange}
                icon={<Image priority src={UnCheck} alt={'icon'} width={24} height={24} />}
                checkedIcon={<Image priority src={Check} alt={'icon'} width={24} height={24} />}
                sx={{
                  padding: '0rem',
                }}
              />
            }
            label={
              <StyledTerms>
                {SIGNUP_PAGE_MESSAGES.ACCEPT} <span>{SIGNUP_PAGE_MESSAGES.TERMS_CONDITION}</span>
              </StyledTerms>
            }
          />
        </StyledFormContainer>

        <CustomButton
          onClick={handleSubmit}
          disabled={loading}
          text={loading ? 'Signing up...' : 'Sign Up'}
          sx={{
            marginTop: '2.06rem',
            backgroundColor: 'var(--sky-blue)',
          }}
        />
      </StyledSignUpFormSubMiniContainer>
    </StyledSignUpFormSubContainer>
  );
};

export default SignUpForm;
