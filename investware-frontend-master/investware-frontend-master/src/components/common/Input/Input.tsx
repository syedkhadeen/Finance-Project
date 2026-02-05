import { StyledInput } from '@/styles/commonStyle';
import { SxProps } from '@mui/material';
import { ReactNode } from 'react';

interface Props {
  sx?: SxProps;
  onChange?: React.ChangeEventHandler<HTMLInputElement | HTMLTextAreaElement>;
  value?: string;
  placeholder?: string;
  label: string;
  type?: string;
  endAdornment?: ReactNode;
}

const Input = ({ sx, value, placeholder, onChange, label, type = 'text', endAdornment }: Props) => {
  return (
    <>
      <StyledInput
        type={type}
        autoComplete="off"
        placeholder={placeholder}
        onChange={onChange}
        value={value}
        variant="outlined"
        label={label}
        focused
        InputProps={{
          endAdornment: endAdornment,
        }}
        sx={{
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
          '& .MuiInputLabel-root': {
            '&.Mui-focused': {
              color: 'rgba(0, 0, 0, 0.60)',
              fontSize: '0.75rem',
              marginTop: '0.2rem',
              fontWeight: '400',
            },
          },
          ...sx,
        }}
      />
    </>
  );
};

export default Input;
