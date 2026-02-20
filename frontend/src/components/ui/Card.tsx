import { type HTMLAttributes } from 'react';
import { cn } from '../../lib/utils';

type CardProps = HTMLAttributes<HTMLDivElement>;

export function Card({ className, children, ...props }: CardProps) {
  return (
    <div
      className={cn(
        'rounded-xl border border-gray-200 bg-white shadow-sm',
        className,
      )}
      {...props}
    >
      {children}
    </div>
  );
}

type CardHeaderProps = HTMLAttributes<HTMLDivElement>;

export function CardHeader({ className, children, ...props }: CardHeaderProps) {
  return (
    <div className={cn('px-5 py-4 border-b border-gray-100', className)} {...props}>
      {children}
    </div>
  );
}

type CardBodyProps = HTMLAttributes<HTMLDivElement>;

export function CardBody({ className, children, ...props }: CardBodyProps) {
  return (
    <div className={cn('px-5 py-4', className)} {...props}>
      {children}
    </div>
  );
}
