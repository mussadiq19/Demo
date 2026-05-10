import { useQuery } from '@tanstack/react-query';
import { companyService } from '../services/companyService';
import { useAuth } from './useAuth';

export function useDashboard() {
  const { user } = useAuth();
  return useQuery({
    queryKey: ['dashboard', user?.companyId],
    queryFn: () => companyService.getDashboard(user.companyId),
    enabled: !!user?.companyId,
    refetchInterval: 30000,
    staleTime: 10000,
  });
}
