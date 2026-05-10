import { useQueries, useQuery } from '@tanstack/react-query';
import { skillsService } from '../services/skillsService';
import { useAuth } from './useAuth';

export function useSkillsGap() {
  const { user } = useAuth();
  const companyGapQuery = useQuery({
    queryKey: ['skills-gap', user?.companyId],
    queryFn: () => skillsService.getGaps(user.companyId),
    enabled: !!user?.companyId,
  });

  const employeeIds = [
    ...new Set([
      ...(companyGapQuery.data?.employeeIds || []),
      ...(companyGapQuery.data?.employees || []).map((employee) => employee.userId ?? employee.id),
      user?.id,
    ].filter(Boolean)),
  ];

  const employeeGapQueries = useQueries({
    queries: employeeIds.map((userId) => ({
      queryKey: ['skills-gap-employee', userId],
      queryFn: () => skillsService.getEmployeeGap(userId),
      enabled: !!companyGapQuery.data && !!userId,
    })),
  });

  return {
    ...companyGapQuery,
    data: companyGapQuery.data
      ? {
          ...companyGapQuery.data,
          employeeGaps: employeeGapQueries
            .map((query) => query.data)
            .filter(Boolean),
        }
      : companyGapQuery.data,
    isLoading: companyGapQuery.isLoading || employeeGapQueries.some((query) => query.isLoading),
  };
}
