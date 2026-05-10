import { useQuery } from '@tanstack/react-query';
import { riskService } from '../services/riskService';
import { useAuth } from './useAuth';

export function useRecentRisks() {
  const { user } = useAuth();
  return useQuery({
    queryKey: ['risks', user?.companyId],
    queryFn: () => riskService.getRisks({
      companyId: user.companyId,
      status: 'OPEN',
      page: 0,
      size: 4,
    }),
    enabled: !!user?.companyId,
    refetchInterval: 30000,
  });
}
