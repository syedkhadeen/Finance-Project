// pages/subscription.tsx  (or app/subscription/page.tsx)

import React, { useState } from 'react';
import {
  Box,
  Typography,
  Button,
  Card,
  CardContent,
  CardActions,
  Grid,
  Container,
  Chip,
  Divider,
  TextField,
  InputAdornment,
  FormHelperText,
} from '@mui/material';
import StarIcon from '@mui/icons-material/Star';
import { styled } from '@mui/material/styles';

const StyledCard = styled(Card, {
  shouldForwardProp: (prop) => prop !== 'recommended' && prop !== 'highlight',
})<{ recommended?: boolean; highlight?: boolean }>(({ theme, recommended, highlight }) => ({
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
  transition: 'all 0.3s ease',
  borderWidth: recommended ? 3 : highlight ? 2 : 1,
  borderColor: recommended
    ? theme.palette.primary.main
    : highlight
    ? theme.palette.secondary.main
    : theme.palette.divider,
  boxShadow: recommended ? theme.shadows[8] : highlight ? theme.shadows[4] : theme.shadows[2],
  '&:hover': {
    transform: 'translateY(-8px)',
    boxShadow: recommended ? theme.shadows[12] : theme.shadows[8],
  },
}));

type PlanName = 'Free' | 'Custom Subscription' | 'Premium Monthly' | 'Premium Yearly';

export default function SubscriptionPage() {
  const currentPlan = 'Free' as PlanName;
  const usage = { current: 8, limit: 15, period: 'this month' };
  const pricePerCalculation = 5;

  const [customCalculations, setCustomCalculations] = useState<number>(50);
  const customTotal = customCalculations * pricePerCalculation;

  const plans = [
    {
      name: 'Free',
      price: '$0',
      interval: 'forever',
      features: [
        '15 calculation requests per month included',
        'Additional requests: $5 each',
        'Basic analytics',
      ],
      isCurrent: currentPlan === 'Free',
      recommended: false,
    },
    {
      name: 'Custom Subscription',
      price: `$${customTotal}`,
      interval: 'one-time',
      features: [
        'Choose exactly how many calculations you need',
        'Pay only for what you pre-purchase',
        'No monthly commitment',
        'Same advanced analytics as Premium',
        'Ideal for occasional or project-based usage',
      ],
      isCurrent: currentPlan === 'Custom Subscription',
      recommended: false,
      highlight: true,
      isCustom: true,
    },
    {
      name: 'Premium Monthly',
      price: '$70',
      interval: 'month',
      features: [
        '100 calculation requests included per month',
        '$3 per calculation after included limit',
        'Priority support',
        'Advanced analytics & reports',
        'Export options',
      ],
      isCurrent: currentPlan === 'Premium Monthly',
      recommended: false,
    },
    {
      name: 'Premium Yearly',
      price: '$199',
      interval: 'year',
      savings: 'Save ~17% (vs $228 yearly)',
      features: [
        'Unlimited calculation requests',
        'Priority support',
        'Advanced analytics & reports',
        'Export options',
        'Best value for frequent/heavy users',
      ],
      isCurrent: currentPlan === 'Premium Yearly',
      recommended: true,
    },
  ];

  return (
    <Container maxWidth="xl" sx={{ py: { xs: 6, md: 10 } }}>
      <Typography
        variant="h3"
        component="h1"
        align="center"
        gutterBottom
        sx={{ fontWeight: 700, mb: 2 }}
      >
        Choose Your Plan
      </Typography>

      <Typography
        variant="h6"
        color="text.secondary"
        align="center"
        sx={{ mb: 6, maxWidth: 800, mx: 'auto' }}
      >
        You're currently on the <strong>Free plan</strong> — 15 calculations per month.
        <br />
        After that, each additional request costs <strong>${pricePerCalculation}</strong>.
      </Typography>

      <Grid container spacing={4} justifyContent="center">
        {plans.map((plan) => (
          <Grid item xs={12} sm={6} md={3} key={plan.name}>
            <StyledCard recommended={plan.recommended} highlight={plan.highlight}>
              <CardContent sx={{ p: 4, flexGrow: 1, display: 'flex', flexDirection: 'column' }}>
                <Box sx={{ position: 'relative', mb: 3 }}>
                  <Typography variant="h5" align="center" gutterBottom sx={{ fontWeight: 600 }}>
                    {plan.name}
                  </Typography>

                  {plan.recommended && (
                    <Chip
                      icon={<StarIcon fontSize="small" />}
                      label="Best Value"
                      color="primary"
                      size="small"
                      sx={{ position: 'absolute', top: -12, right: -12 }}
                    />
                  )}

                  {plan.highlight && (
                    <Chip
                      label="Flexible"
                      color="secondary"
                      size="small"
                      sx={{ position: 'absolute', top: -12, right: -12 }}
                    />
                  )}

                  {plan.isCurrent && (
                    <Chip
                      label="Current Plan"
                      color="success"
                      size="small"
                      sx={{ position: 'absolute', top: -12, left: -12 }}
                    />
                  )}
                </Box>

                <Box sx={{ textAlign: 'center', mb: 4 }}>
                  <Typography
                    variant="h3"
                    color="primary"
                    sx={{ fontWeight: 700, lineHeight: 1 }}
                  >
                    {plan.price}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {plan.isCustom ? 'total' : `per ${plan.interval}`}
                  </Typography>
                  {plan.savings && (
                    <Typography variant="caption" color="success.main" sx={{ display: 'block', mt: 1 }}>
                      {plan.savings}
                    </Typography>
                  )}
                </Box>

                {plan.isCustom && (
                  <Box sx={{ mb: 4 }}>
                    <TextField
                      fullWidth
                      type="number"
                      label="How many calculations?"
                      value={customCalculations}
                      onChange={(e) => {
                        const val = Number(e.target.value);
                        if (!isNaN(val) && val >= 1) setCustomCalculations(val);
                      }}
                      InputProps={{
                        startAdornment: <InputAdornment position="start"></InputAdornment>,
                      }}
                      helperText={`$${customTotal} for ${customCalculations} calculations`}
                      sx={{ mb: 1 }}
                    />
                    <FormHelperText sx={{ textAlign: 'center' }}>
                      $5 per calculation • One-time payment
                    </FormHelperText>
                  </Box>
                )}

                <Divider sx={{ my: 3 }} />

                <Box component="ul" sx={{ m: 0, p: 0, listStyle: 'none' }}>
                  {plan.features.map((feature, i) => (
                    <Box
                      component="li"
                      key={i}
                      sx={{
                        display: 'flex',
                        alignItems: 'flex-start',
                        mb: 2,
                        '&::before': {
                          content: '"✓"',
                          color: 'success.main',
                          fontWeight: 'bold',
                          mr: 2,
                          mt: 0.3,
                        },
                      }}
                    >
                      <Typography variant="body2">{feature}</Typography>
                    </Box>
                  ))}
                </Box>
              </CardContent>

              <CardActions sx={{ p: 3, pt: 0 }}>
                {plan.isCurrent ? (
                  <Button variant="outlined" color="success" fullWidth disabled>
                    Current Plan
                  </Button>
                ) : (
                  <Button
                    variant={plan.recommended || plan.highlight ? 'contained' : 'outlined'}
                    color={plan.highlight ? 'secondary' : 'primary'}
                    size="large"
                    fullWidth
                    sx={{
                      py: 1.5,
                      fontWeight: 600,
                      textTransform: 'none',
                      fontSize: '1.1rem',
                    }}
                    onClick={() => {
                      if (plan.isCustom) {
                        alert(
                          `Selected: ${customCalculations} calculations → $${customTotal}`
                        );
                      } else {
                        alert(`Selected plan: ${plan.name}`);
                      }
                      // → Real implementation: create checkout session
                    }}
                  >
                    {plan.isCustom ? 'Purchase Now' : 'Select Plan'}
                  </Button>
                )}
              </CardActions>
            </StyledCard>
          </Grid>
        ))}
      </Grid>

      <Box sx={{ mt: 10, textAlign: 'center' }}>
        <Typography variant="body2" color="text.secondary">
          All plans are secure • Cancel anytime • Powered by Stripe
        </Typography>
      </Box>
    </Container>
  );
}