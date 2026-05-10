import { useQuery } from '@tanstack/react-query';
import { companyService } from '../services/companyService';
import { useAuth } from './useAuth';

export function useAiReadiness() {
  const { user } = useAuth();
  return useQuery({
    queryKey: ['ai-readiness', user?.companyId],
    queryFn: () => companyService.getAiReadiness(user.companyId),
    enabled: !!user?.companyId,
  });
}
