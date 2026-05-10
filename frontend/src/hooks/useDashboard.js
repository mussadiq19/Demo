import { useQuery } from '@tanstack/react-query';
import axiosInstance from '../lib/axios';
import { useAuth } from './useAuth';

export function useDashboard() {
  const { user } = useAuth();
  return useQuery({
    queryKey: ['dashboard', user?.companyId],
    queryFn: () => axiosInstance
      .get(`/companies/${user.companyId}/dashboard`)
      .then(r => r.data.data),
    enabled: !!user?.companyId,
  });
}
