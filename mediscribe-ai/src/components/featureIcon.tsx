import { GRADIENT_PRIMARY } from '@/constants';

export const FeatureIcon = ({
  icon,
  gradient = GRADIENT_PRIMARY,
  className = '',
}) => (
  <div
    className={`rounded-full bg-gradient-to-br ${gradient} flex items-center justify-center shadow-lg ${className}`}
  >
    {icon}
  </div>
);
