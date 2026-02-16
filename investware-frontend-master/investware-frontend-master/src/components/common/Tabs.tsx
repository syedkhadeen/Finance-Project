import React, { useState } from 'react';
import { Tabs as MuiTabs, Tab, Box } from '@mui/material';

interface TabItem {
  label: string;
  content: React.ReactNode;
  disabled?: boolean;
}

interface TabsProps {
  tabs: TabItem[];
  defaultTab?: number;
  onChange?: (index: number) => void;
}

export const Tabs: React.FC<TabsProps> = ({ tabs, defaultTab = 0, onChange }) => {
  const [value, setValue] = useState(defaultTab);

  const handleChange = (_event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
    onChange?.(newValue);
  };

  return (
    <Box sx={{ width: '100%' }}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <MuiTabs
          value={value}
          onChange={handleChange}
          aria-label="tabs"
          sx={{
            '& .MuiTab-root': {
              textTransform: 'none',
              fontWeight: 500,
              fontSize: '0.875rem',
              minHeight: 48,
            },
          }}
        >
          {tabs.map((tab, index) => (
            <Tab key={index} label={tab.label} disabled={tab.disabled} />
          ))}
        </MuiTabs>
      </Box>
      {tabs.map((tab, index) => (
        <Box
          key={index}
          role="tabpanel"
          hidden={value !== index}
          sx={{ py: 3 }}
        >
          {value === index && tab.content}
        </Box>
      ))}
    </Box>
  );
};
