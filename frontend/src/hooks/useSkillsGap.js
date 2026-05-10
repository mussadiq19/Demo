import { useQuery } from '@tanstack/react-query';
import { skillsService } from '../services/skillsService';
import { useAuth } from './useAuth';

export function useSkillsGap() {
  const { user } = useAuth();
  return useQuery({
    queryKey: ['skills-gap', user?.companyId],
    queryFn: () => skillsService.getGaps(user.companyId),
    enabled: !!user?.companyId,
  });
}
