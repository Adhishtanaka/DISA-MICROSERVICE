import { useState } from 'react';
import { useNavigate, Link } from 'react-router';
import { Input } from '../../../components/ui/Input';
import { Button } from '../../../components/ui/Button';
import { Select } from '../../../components/ui/Select';
import { useRegister } from '../hooks/useAuth';
import type { UserRole } from '../types/auth.types';

const roleOptions = [
  { value: 'ADMIN', label: 'Admin' },
  { value: 'COORDINATOR', label: 'Coordinator' },
  { value: 'RESPONDER', label: 'Responder' },
  { value: 'VOLUNTEER', label: 'Volunteer' },
];

export function RegisterForm() {
  const navigate = useNavigate();
  const { register, loading, error } = useRegister();
  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
    fullName: '',
    phoneNumber: '',
    role: 'VOLUNTEER' as UserRole,
  });

  const set = (field: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
    setForm((f) => ({ ...f, [field]: e.target.value }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await register({
        ...form,
        phoneNumber: form.phoneNumber || undefined,
      });
      navigate('/missions');
    } catch {
      // error displayed via hook state
    }
  };

  return (
    <div className="mx-auto w-full max-w-sm">
      <div className="rounded-xl border border-gray-200 bg-white p-8 shadow-sm">
        <div className="mb-6 text-center">
          <h1 className="text-2xl font-bold text-gray-900">DISA</h1>
          <p className="mt-1 text-sm text-gray-500">Create your account</p>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <Input
            label="Full Name"
            placeholder="John Doe"
            value={form.fullName}
            onChange={set('fullName')}
            required
          />

          <Input
            label="Username"
            placeholder="johndoe"
            value={form.username}
            onChange={set('username')}
            required
          />

          <Input
            label="Email"
            type="email"
            placeholder="john@example.com"
            value={form.email}
            onChange={set('email')}
            required
          />

          <Input
            label="Phone Number"
            type="tel"
            placeholder="+1 234 567 8900"
            value={form.phoneNumber}
            onChange={set('phoneNumber')}
          />

          <Input
            label="Password"
            type="password"
            placeholder="Min. 6 characters"
            value={form.password}
            onChange={set('password')}
            required
          />

          <Select
            label="Role"
            options={roleOptions}
            value={form.role}
            onChange={set('role')}
          />

          {error && (
            <p className="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-600">{error}</p>
          )}

          <Button type="submit" isLoading={loading} className="mt-1 w-full">
            Create account
          </Button>
        </form>

        <p className="mt-5 text-center text-sm text-gray-500">
          Already have an account?{' '}
          <Link to="/login" className="font-medium text-blue-600 hover:underline">
            Sign in
          </Link>
        </p>
      </div>
    </div>
  );
}
